package com.example.project_card.applys.service;

import com.example.project_card.applys.domain.NoSeq;
import com.example.project_card.applys.domain.ReceiveApply;
import com.example.project_card.applys.domain.SeqNo;
import com.example.project_card.applys.dto.request.ReceiveApplyDTO;
import com.example.project_card.applys.dto.response.ReceiveApplyElementDTO;
import com.example.project_card.applys.exception.ReceiveApplyErrorCode;
import com.example.project_card.applys.exception.BillErrorCode;
import com.example.project_card.applys.repository.NoSeqRepository;
import com.example.project_card.applys.repository.ReceiveApplyRepository;
import com.example.project_card.applys.repository.SeqNoRepository;
import com.example.project_card.cards.domain.Brand;
import com.example.project_card.cards.domain.Card;
import com.example.project_card.cards.exception.CardErrorCode;
import com.example.project_card.users.exception.CustomerErrorCode;
import com.example.project_card.cards.repository.CardRepository;
import com.example.project_card.comCodes.domain.ComCodeDtl;
import com.example.project_card.comCodes.repository.ComCodeDtlRepository;
import com.example.project_card.global.exception.BaseException;
import com.example.project_card.users.domain.Bill;
import com.example.project_card.users.domain.Customer;
import com.example.project_card.users.repository.BillRepository;
import com.example.project_card.users.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ApplyService {

    private final ReceiveApplyRepository receiveApplyRepository;
    private final NoSeqRepository noSeqRepository;
    private final ComCodeDtlRepository comCodeDtlRepository;
    private final CardRepository cardRepository;
    private final SeqNoRepository seqNoRepository;
    private final CustomerRepository customerRepository;
    private final BillRepository billRepository;

    public List<ReceiveApplyElementDTO> SelectReceiveApplyList(String rcvD1, String rcvD2, String applClas, String ssn1, String ssn2)
    {
        log.info("{ ApplyService } : SelectReceiveApplyList 조회");
        log.info(" >> rcvD1 - "+rcvD1);
        log.info(" >> rcvD2 - "+rcvD2);
        log.info(" >> applClas - "+applClas);
        log.info(" >> ssn1 - "+ssn1);
        log.info(" >> ssn2 - "+ssn2);

        String ssn = "";
        if(ssn1 != null && !ssn1.equals(""))
            ssn = ssn1 + "-" + ssn2;
        log.info(" >> ssn - "+ssn);
        List<ReceiveApply> receiveApplyList = receiveApplyRepository.findAllByRcvD1AndRcvD2AndApplClasAndSsnOrderByRcvD(rcvD1, rcvD2, applClas, ssn);
        log.info("size = "+receiveApplyList.size());
        List<ReceiveApplyElementDTO> receiveApplyElementDTOList = new ArrayList<>();
        for(ReceiveApply receiveApply: receiveApplyList)
        {
            ComCodeDtl comCodeDtl = comCodeDtlRepository.findByGroupCdAndCode("C007", receiveApply.getApplClas());
            String applClasNm = comCodeDtl.getCodeNm();

            String codeNm = "";
            if(receiveApply.getImpsbClas() != null && receiveApply.getImpsbClas().equals("1"))
            {
                comCodeDtl = comCodeDtlRepository.findByGroupCdAndCode("C001", receiveApply.getImpsbCd());
                codeNm = comCodeDtl.getCodeNm();
            }
            receiveApplyElementDTOList.add(ReceiveApplyElementDTO.fromReceiveApplyElement(receiveApply, applClasNm, codeNm));
        }

        log.info("{ ApplyService } : SelectReceiveApplyList 조회 완료");
        return receiveApplyElementDTOList;
    }

    public ReceiveApplyDTO ReceiveApplyModify(ReceiveApplyDTO receiveApplyDTO)
    {
        log.info("{ ApplyService } : ReceiveApplyModify 수정");
        String ssn = receiveApplyDTO.ssn1() + "-" +receiveApplyDTO.ssn2();
        /* 불능 체크 */
        ComCodeDtl comCodeDtl = comCodeDtlRepository.findByGroupCdAndCode("C007", receiveApplyDTO.applClas());
        if(comCodeDtl == null)
            throw BaseException.type(ReceiveApplyErrorCode.APPLY_CLAS_NOT_FOUND);

        /* 수정 가능 여부 */
        ReceiveApply receiveApply = receiveApplyRepository.findBySsnAndRcvDAndRcvSeqNo(ssn, String.valueOf(receiveApplyDTO.rcvD()), receiveApplyDTO.rcvSeqNo());
        // 입회 신청 정보가 없는 경우
        if(receiveApply == null)
            throw BaseException.type(ReceiveApplyErrorCode.RECEIVE_APPLY_NOT_FOUND);
        // 이미 카드를 발급받은 경우
        if(receiveApply.getCrdNo() != null)
            throw BaseException.type(ReceiveApplyErrorCode.RECEIVE_APPLY_CANNOT_MODIFIED);

        /* 수정하는 신청구분 */
        String applClasCodeNm = comCodeDtl.getCodeNm();
        switch (applClasCodeNm) {
            case "최초신규" -> {
                // 기존카드 소지 여부
                Long existCard = cardRepository.countBySsn(ssn);
                if (existCard > 0) // 불능코드 04 (기존카드 존재)
                    return UpdateReceiveApply(receiveApply, applClasCodeNm, receiveApplyDTO, "1", "04");
            }
            case "추가신규" -> {
                // 기존카드 소지 여부 (자유인 체크)
                Long existCard = cardRepository.countBySsn(ssn);
                log.info(" >> count : "+existCard);
                if (existCard == 0) // 불능코드 05 (기존카드 미존재)
                    return UpdateReceiveApply(receiveApply, applClasCodeNm, receiveApplyDTO, "1", "05");

                // 동일 브랜드 카드 소지 여부
                Long existBrdCard = cardRepository.countBySsnAndBrd(ssn, receiveApplyDTO.brd());
                if (existBrdCard > 0) // 불능코드 04 (기존카드 존재)
                    return UpdateReceiveApply(receiveApply, applClasCodeNm, receiveApplyDTO, "1", "04");
            }
            case "재발급" -> {
                // 기존카드 소지 여부 (자유인 체크)
                Long existCard = cardRepository.countBySsn(ssn);
                if (existCard == 0) // 불능코드 05 (기존카드 미존재)
                    return UpdateReceiveApply(receiveApply, applClasCodeNm, receiveApplyDTO, "1", "05");

                // 동일 브랜드 최종카드 소지 여부
                Card lstBrdCard = cardRepository.findBySsnAndBrdAndLstCrdF(ssn, receiveApplyDTO.brd(), "1");
                if (lstBrdCard == null) // 불능코드 05 (기존카드 미존재)
                    return UpdateReceiveApply(receiveApply, applClasCodeNm, receiveApplyDTO, "1", "05");
            }
        }

        /* 당일 가입신청 검증 */
        if(comCodeDtl.getCodeNm().equals("최초신규") || comCodeDtl.getCodeNm().equals("추가신규"))
        {
            ReceiveApply PsbReceiveApply = receiveApplyRepository.findBySsnAndRcvDAndImpsbClas(ssn, String.valueOf(receiveApplyDTO.rcvD()), "");
            if(PsbReceiveApply != null) // 불능코드 01 (당일신청내역 존재)
                return UpdateReceiveApply(PsbReceiveApply, applClasCodeNm, receiveApplyDTO, "1", "01");
        }

        /* 결제계좌 검증 */
        if((receiveApplyDTO.stlAct() != null && !receiveApplyDTO.stlAct().equals(""))
                && !ValidStlAct(receiveApplyDTO.stlAct())) // 불능코드 02 (결제계좌 오류)
            return UpdateReceiveApply(receiveApply, applClasCodeNm, receiveApplyDTO, "1", "02");

        /* 비밀번호 검증 */
        if (!ValidScrtNo(receiveApplyDTO)) // 불능코드 03 (비밀번호 오류)
            return UpdateReceiveApply(receiveApply, applClasCodeNm, receiveApplyDTO, "1", "03");

        /* 정상 등록 */
        return UpdateReceiveApply(receiveApply, applClasCodeNm, receiveApplyDTO, "", "");
    }


    public ReceiveApplyDTO ReceiveApply(ReceiveApplyDTO receiveApplyDTO)
    {
        log.info("{ ApplyService } : ReceiveApply 생성");
        String ssn = receiveApplyDTO.ssn1() + "-" +receiveApplyDTO.ssn2();

        /* 불능 체크 */
        ComCodeDtl comCodeDtl = comCodeDtlRepository.findByGroupCdAndCode("C007", receiveApplyDTO.applClas());
        if(comCodeDtl == null)
            throw BaseException.type(ReceiveApplyErrorCode.APPLY_CLAS_NOT_FOUND);

        String applClasCodeNm = comCodeDtl.getCodeNm();
        switch (applClasCodeNm) {
            case "최초신규" -> {
                // 기존카드 소지 여부
                Long existCard = cardRepository.countBySsn(ssn);
                if (existCard > 0) // 불능코드 04 (기존카드 존재)
                    return SaveReceiveApply(ssn, applClasCodeNm, receiveApplyDTO, "1", "04");
            }
            case "추가신규" -> {
                // 기존카드 소지 여부 (자유인 체크)
                Long existCard = cardRepository.countBySsn(ssn);
                log.info(" >> count : "+existCard);
                if (existCard == 0) // 불능코드 05 (기존카드 미존재)
                    return SaveReceiveApply(ssn, applClasCodeNm, receiveApplyDTO, "1", "05");

                // 동일 브랜드 카드 소지 여부
                Long existBrdCard = cardRepository.countBySsnAndBrd(ssn, receiveApplyDTO.brd());
                if (existBrdCard > 0) // 불능코드 04 (기존카드 존재)
                    return SaveReceiveApply(ssn, applClasCodeNm, receiveApplyDTO, "1", "04");
            }
            case "재발급" -> {
                // 기존카드 소지 여부 (자유인 체크)
                Long existCard = cardRepository.countBySsn(ssn);
                if (existCard == 0) // 불능코드 05 (기존카드 미존재)
                    return SaveReceiveApply(ssn, applClasCodeNm, receiveApplyDTO, "1", "05");

                // 동일 브랜드 최종카드 소지 여부
                Card lstBrdCard = cardRepository.findBySsnAndBrdAndLstCrdF(ssn, receiveApplyDTO.brd(), "1");
                if (lstBrdCard == null) // 불능코드 05 (기존카드 미존재)
                    return SaveReceiveApply(ssn, applClasCodeNm, receiveApplyDTO, "1", "05");
            }
        }

        /* 당일 가입신청 검증 */
        if(comCodeDtl.getCodeNm().equals("최초신규") || comCodeDtl.getCodeNm().equals("추가신규"))
        {
            ReceiveApply PsbReceiveApply = receiveApplyRepository.findBySsnAndRcvDAndImpsbClas(ssn, String.valueOf(receiveApplyDTO.rcvD()), "");
            if(PsbReceiveApply != null) // 불능코드 01 (당일신청내역 존재)
                return SaveReceiveApply(ssn, applClasCodeNm, receiveApplyDTO, "1", "01");
        }

        /* 결제계좌 검증 */
        if((receiveApplyDTO.stlAct() != null && !receiveApplyDTO.stlAct().equals(""))
                && !ValidStlAct(receiveApplyDTO.stlAct())) // 불능코드 02 (결제계좌 오류)
            return SaveReceiveApply(ssn, applClasCodeNm, receiveApplyDTO, "1", "02");

        /* 비밀번호 검증 */
        if (!ValidScrtNo(receiveApplyDTO)) // 불능코드 03 (비밀번호 오류)
            return SaveReceiveApply(ssn, applClasCodeNm, receiveApplyDTO, "1", "03");

        /* 정상 등록 */
        return SaveReceiveApply(ssn, applClasCodeNm, receiveApplyDTO, "", "");
    }

    public boolean ValidScrtNo(ReceiveApplyDTO receiveApplyDTO)
    {
        String scrtNo = receiveApplyDTO.scrtNo();

        // 6자리가 아닌 경우
        if(scrtNo.length() != 6)
            return false;

        // 생년월일과 동일할 경우
        if(receiveApplyDTO.birthD().substring(2, 8).equals(scrtNo))
            return false;

        // 전화번호 앞뒷자리와 동일한 경우
        String[] hdpNo = receiveApplyDTO.hdpNo().split("-");
        String hdpNo1 = hdpNo[1];
        String hdpNo2 = hdpNo[2];
        if(scrtNo.contains(hdpNo1) || scrtNo.contains(hdpNo2))
            return false;

        // 동일 번호 존재할 경우
        String[] sameNumbers = {"0000", "1111", "2222", "3333", "4444", "5555", "6666", "7777", "8888", "9999"};
        for(String num: sameNumbers)
        {
            if(scrtNo.contains(num))
                return false;
        }

        // 연속 번호 존재할 경우
        String[] sequentialNumbers = {"0123", "1234", "2345", "3456", "4567", "5678", "6789", "9876", "8765", "7654", "6543", "5432", "4321", "3210"};
        for(String num: sequentialNumbers)
        {
            if(scrtNo.contains(num))
                return false;
        }

        return true;
    }

    public boolean ValidStlAct(String stlAct)
    {
        return stlAct.equals("1");
    }

    public String GenerateNoSeq(String now)
    {
        String maxNoSeq = noSeqRepository.findMaxNoSeqByRcvD(now);
        return (maxNoSeq == null) ? "1" : String.valueOf(Integer.parseInt(maxNoSeq) + 1);
    }

    public NoSeq CreateNoSeq()
    {
        String now = LocalDate.now().toString();
        String rcvSeqNo = GenerateNoSeq(now);
        NoSeq noSeq = NoSeq.createNoSeq(
                now,
                rcvSeqNo
        );
        return noSeqRepository.save(noSeq);
    }

    public ReceiveApply CreateReceiveApply(String ssn, String rcvSeqNo, ReceiveApplyDTO receiveApplyDTO, String impsbClas, String impsbCd)
    {
        ReceiveApply receiveApply = ReceiveApply.createReceiveApply(
                ssn,
                receiveApplyDTO.rcvD().toString(),
                rcvSeqNo,
                receiveApplyDTO.applD(),
                receiveApplyDTO.birthD(),
                receiveApplyDTO.hgNm(),
                receiveApplyDTO.engNm(),
                receiveApplyDTO.stlMtd(),
                receiveApplyDTO.stlAct(),
                receiveApplyDTO.bnkCd(),
                receiveApplyDTO.stlDd(),
                "", // 관리 영업점
                receiveApplyDTO.applClas(),
                receiveApplyDTO.stmtSndMtd(),
                receiveApplyDTO.billAdr1(),
                receiveApplyDTO.billAdr2(),
                receiveApplyDTO.billZip(),
                receiveApplyDTO.hdpNo(),
                receiveApplyDTO.brd(),
                receiveApplyDTO.scrtNo(),
                receiveApplyDTO.emailAdr1().equals("") ? "" : receiveApplyDTO.emailAdr1()+ "@" + receiveApplyDTO.emailAdr2(),
                "",
                impsbClas,
                impsbCd
        );
        return receiveApplyRepository.save(receiveApply);
    }
    public void ModifyReceiveApply(ReceiveApply receiveApply, ReceiveApplyDTO receiveApplyDTO, String impsbClas, String impsbCd)
    {
        receiveApply.updateReceiveApply(
                receiveApplyDTO.applD(),
                receiveApplyDTO.birthD(),
                receiveApplyDTO.hgNm(),
                receiveApplyDTO.engNm(),
                receiveApplyDTO.stlMtd(),
                receiveApplyDTO.stlAct(),
                receiveApplyDTO.bnkCd(),
                receiveApplyDTO.stlDd(),
                "", // 관리 영업점
                receiveApplyDTO.applClas(),
                receiveApplyDTO.stmtSndMtd(),
                receiveApplyDTO.billAdr1(),
                receiveApplyDTO.billAdr2(),
                receiveApplyDTO.billZip(),
                receiveApplyDTO.hdpNo(),
                receiveApplyDTO.brd(),
                receiveApplyDTO.scrtNo(),
                receiveApplyDTO.emailAdr1().equals("") ? "" : receiveApplyDTO.emailAdr1()+ "@" + receiveApplyDTO.emailAdr2(),
                "",
                impsbClas,
                impsbCd
        );
    }

    public ReceiveApplyDTO UpdateReceiveApply(ReceiveApply receiveApply, String applClasCodeNm, ReceiveApplyDTO receiveApplyDTO, String impsbClas, String impsbCd)
    {
        /* 접수일련번호 이미 있음 */
        NoSeq noSeq = noSeqRepository.findByRcvDAndRcvSeqNo(receiveApply.getRcvD(), receiveApply.getRcvSeqNo());

        // 수정
        ModifyReceiveApply(receiveApply, receiveApplyDTO, impsbClas, impsbCd);

        String codeNm = "";
        if(impsbClas.equals("1"))
        {
            ComCodeDtl comCodeDtl = comCodeDtlRepository.findByGroupCdAndCode("C001", impsbCd);
            codeNm = comCodeDtl.getCodeNm();
        }

        ReceiveApplyDTO newReceiveApplyDTO = ReceiveApplyDTO.fromReceiveApply(receiveApply, codeNm);
        log.info("{ ApplyService } : ReceiveApply 수정 완료");

        /* 정상 등록시 (고객/결제/카드 테이블 insert 또는 update) */
        if(!impsbClas.equals("1"))
            SavePsbReceiveApply(receiveApply.getSsn(), noSeq, applClasCodeNm, receiveApplyDTO);

        return newReceiveApplyDTO;
    }

    public ReceiveApplyDTO SaveReceiveApply(String ssn, String applClasCodeNm, ReceiveApplyDTO receiveApplyDTO, String impsbClas, String impsbCd)
    {
        /* 접수일련번호 발번 */
        NoSeq noSeq = CreateNoSeq();
        String rcvSeqNo = noSeq.getRcvSeqNo();

        // 등록
        ReceiveApply newReceiveApply = CreateReceiveApply(ssn, rcvSeqNo, receiveApplyDTO, impsbClas, impsbCd);

        String codeNm = "";
        if(impsbClas.equals("1"))
        {
            ComCodeDtl comCodeDtl = comCodeDtlRepository.findByGroupCdAndCode("C001", impsbCd);
            codeNm = comCodeDtl.getCodeNm();
        }

        log.info("newReceiveApply : "+newReceiveApply.getEmailAdr());
        ReceiveApplyDTO newReceiveApplyDTO = ReceiveApplyDTO.fromReceiveApply(newReceiveApply, codeNm);
        log.info("{ ApplyService } : ReceiveApply 생성 완료");

        /* 정상 등록시 (고객/결제/카드 테이블 insert 또는 update) */
        if(!impsbClas.equals("1"))
            SavePsbReceiveApply(ssn, noSeq, applClasCodeNm, receiveApplyDTO);

        return newReceiveApplyDTO;
    }

    public String GenerateCustNo()
    {
        String maxCustNo = seqNoRepository.findMaxCustNo();
        return (maxCustNo == null) ? "1" : String.valueOf(Integer.parseInt(maxCustNo) + 1);
    }

    public String GenerateCrdNo()
    {
        String maxCrdNo = seqNoRepository.findMaxCrdNo();
        return (maxCrdNo == null) ? "111111111" : String.valueOf(Long.parseLong(maxCrdNo) + 1);
    }

    public void SavePsbReceiveApply(String ssn, NoSeq noSeq, String applClasCodeNm, ReceiveApplyDTO receiveApplyDTO)
    {
        log.info("{ ApplyService } : SavePsbReceiveApply 정상등록 생성");
        String custNo = ""; // 고객 번호
        String cN = ""; // 카드 번호

        ComCodeDtl comCodeDtl = comCodeDtlRepository.findByGroupCdAndCode("C004", receiveApplyDTO.brd());
        if(comCodeDtl == null)
            throw BaseException.type(ReceiveApplyErrorCode.BRD_NOT_FOUND);
        String codeNm = comCodeDtl.getCodeNm();

        switch (applClasCodeNm) {
            case "최초신규" -> {
                /* 고객번호 발번 */
                custNo = GenerateCustNo();

                // 고객 insert
                Customer customer = Customer.createCustomer(
                        custNo,
                        ssn,
                        String.valueOf(LocalDate.now()),
                        receiveApplyDTO.hgNm(),
                        receiveApplyDTO.birthD(),
                        receiveApplyDTO.hdpNo()
                );
                customerRepository.save(customer);

                // 결제 insert
                Bill bill = Bill.createBill(
                        receiveApplyDTO.stlAct(),
                        receiveApplyDTO.bnkCd(),
                        receiveApplyDTO.hgNm(), // 예금주명
                        receiveApplyDTO.stlMtd(),
                        receiveApplyDTO.stlDd(),
                        custNo,
                        receiveApplyDTO.prcsClas(),
                        receiveApplyDTO.stmtSndMtd(),
                        receiveApplyDTO.stmtSndMtd().equals("3") ? "1" : "",
                        receiveApplyDTO.billZip(),
                        receiveApplyDTO.billAdr1(),
                        receiveApplyDTO.billAdr2(),
                        receiveApplyDTO.emailAdr1().equals("") ? "" : receiveApplyDTO.emailAdr1()+ "@" + receiveApplyDTO.emailAdr2()
                );
                billRepository.save(bill);

                // 카드 insert
                Card card = CreateCard(receiveApplyDTO, codeNm, custNo, ssn, "");
                cN = card.getCrdNo().substring(6, 15);

                // 신청 update
                UpdateApply(ssn, noSeq, card.getCrdNo());
            }
            case "추가신규" -> {
                // 고객 update
                Customer customer = UpdateCustomer(receiveApplyDTO, ssn);
                custNo = customer.getCustNo();

                // 결제 update
                UpdateBill(receiveApplyDTO, custNo);

                // 카드 insert
                Card card = CreateCard(receiveApplyDTO, codeNm, custNo, ssn, "");
                cN = card.getCrdNo().substring(6, 15);

                // 신청 update
                UpdateApply(ssn, noSeq, card.getCrdNo());
            }
            case "재발급" -> {
                // 고객 update
                Customer customer = UpdateCustomer(receiveApplyDTO, ssn);
                custNo = customer.getCustNo();

                // 결제 update
                UpdateBill(receiveApplyDTO, custNo);

                // 기존소지카드 update
                Card lstBrdCard = cardRepository.findBySsnAndBrdAndLstCrdF(ssn, receiveApplyDTO.brd(), "1");
                if(lstBrdCard == null)
                    throw BaseException.type(CardErrorCode.CARD_NOT_FOUND);
                lstBrdCard.updateLstCrdF();

                // 신규카드 insert
                Card card = CreateCard(receiveApplyDTO, codeNm, custNo, ssn, lstBrdCard.getBfCrdNo());
                cN = card.getCrdNo().substring(6, 15);

                // 신청 update
                UpdateApply(ssn, noSeq, card.getCrdNo());
            }
        }

        /* 관리번호 발번 */
        log.info(" >> custNo : "+ custNo);
        log.info(" >> crdNo : "+cN);
        SeqNo seqNo = CreateSeqNo(custNo, cN);

        log.info("{ ApplyService } : SavePsbReceiveApply 정상등록 생성 완료");
    }

    public SeqNo CreateSeqNo(String custNo, String cN)
    {
        SeqNo seqNo = SeqNo.createSeqNo(
                custNo,
                cN
        );
        return seqNoRepository.save(seqNo);
    }

    public void UpdateApply(String ssn, NoSeq noSeq, String crdNo)
    {
        ReceiveApply receiveApply = receiveApplyRepository.findBySsnAndRcvDAndRcvSeqNo(ssn, noSeq.getRcvD(), noSeq.getRcvSeqNo());
        receiveApply.updateReceiveApplyCrdNo(crdNo);
    }

    public Customer UpdateCustomer(ReceiveApplyDTO receiveApplyDTO, String ssn)
    {
        Customer customer = customerRepository.findBySsn(ssn);
        if(customer == null)
            throw BaseException.type(CustomerErrorCode.CUSTOMER_NOT_FOUND);
        customer.updateCustomer(receiveApplyDTO.hgNm(), receiveApplyDTO.birthD(), receiveApplyDTO.hdpNo());
        return customer;
    }

    public void UpdateBill(ReceiveApplyDTO receiveApplyDTO, String custNo)
    {
        Bill bill = billRepository.findByCustNo(custNo);
        if(bill == null)
            throw BaseException.type(BillErrorCode.BILL_NOT_FOUND);
        bill.updateBill(
                receiveApplyDTO.stlAct(),
                receiveApplyDTO.bnkCd(),
                receiveApplyDTO.hgNm(), // 예금주명
                receiveApplyDTO.stlMtd(),
                receiveApplyDTO.stlDd(),
                receiveApplyDTO.prcsClas(),
                receiveApplyDTO.stmtSndMtd(),
                receiveApplyDTO.stmtSndMtd().equals("3") ? "1" : "",
                receiveApplyDTO.billZip(),
                receiveApplyDTO.billAdr1(),
                receiveApplyDTO.billAdr2(),
                receiveApplyDTO.emailAdr1().equals("") ? "" : receiveApplyDTO.emailAdr1()+ "@" + receiveApplyDTO.emailAdr2()
        );
    }

    public Card CreateCard(ReceiveApplyDTO receiveApplyDTO, String codeNm, String custNo, String ssn, String bfCrdNo)
    {

        /* 카드번호 발번 */
        String cN = GenerateCrdNo(); // 일련번호 9자리
        String crdGrd = "11"; // 등급 2자리

        String brdNo = Brand.valueOf(codeNm).getNumber(); // 브랜드 4자리
        String crdNo = checkCrdNo(brdNo + crdGrd + cN);

        Card card = Card.createCard(
                crdNo,
                custNo,
                "", // mgtBbrn
                String.valueOf(LocalDate.now()), // 등록일자
                ssn,
                CalculateVldDur(),  // 해당 월 포함 +5년
                receiveApplyDTO.brd(),
                receiveApplyDTO.scrtNo(),
                receiveApplyDTO.engNm(),
                bfCrdNo, // 전 카드 번호
                "1", // 최종 카드 여부
                String.valueOf(LocalDate.now()), // 최초 카드 등록일자
                "11" // 카드 등급 (11 일반 고정)
        );
        return cardRepository.save(card);
    }

    public ReceiveApplyDTO ReceiveApplyDetail(ReceiveApplyDTO receiveApplyDTO)
    {
        log.info("{ ApplyService } : ReceiveApplyDetail 조회");
        String ssn = receiveApplyDTO.ssn1() + "-" +receiveApplyDTO.ssn2();
        ReceiveApply receiveApply = receiveApplyRepository.findBySsnAndRcvDAndRcvSeqNo(ssn, String.valueOf(receiveApplyDTO.rcvD()), receiveApplyDTO.rcvSeqNo());
        // 입회 신청 정보가 없는 경우
        if(receiveApply == null)
            throw BaseException.type(ReceiveApplyErrorCode.RECEIVE_APPLY_NOT_FOUND);
        // 이미 카드를 발급받은 경우
        if(receiveApply.getCrdNo() != null)
            throw BaseException.type(ReceiveApplyErrorCode.RECEIVE_APPLY_CANNOT_MODIFIED);

        ComCodeDtl comCodeDtl = comCodeDtlRepository.findByGroupCdAndCode("C001", receiveApply.getImpsbCd());
        String codeNm = comCodeDtl.getCodeNm();

        receiveApplyDTO = ReceiveApplyDTO.fromReceiveApply(receiveApply, codeNm);
        log.info("{ ApplyService } : ReceiveApplyDetail 조회 완료");
        return receiveApplyDTO;
    }



    public String checkCrdNo(String number)
    {
        int[] checkDigit = {2,3,4,5,6,7,8,9,2,3,4,5,6,7,8};
        char[] crd = number.toCharArray();
        int sum = 0;
        for(int i = 0; i < 15; i++)
        {
            sum += checkDigit[i] * (crd[i] - '0');
        }

        sum %= 11;
        sum = 11 - sum;

        // 16자리 반환
        return number + (char) (sum + '0');
    }

    public String CalculateVldDur()
    {
        LocalDate endDate = LocalDate.now().plusYears(5).minusMonths(1);
        return endDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }
}
