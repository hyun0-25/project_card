package com.example.project_card.applys.service;

import com.example.project_card.applys.domain.NoSeq;
import com.example.project_card.applys.domain.ReceiveApply;
import com.example.project_card.applys.dto.request.ReceiveApplyDTO;
import com.example.project_card.applys.exception.ApplyErrorCode;
import com.example.project_card.applys.repository.NoSeqRepository;
import com.example.project_card.applys.repository.ReceiveApplyRepository;
import com.example.project_card.applys.repository.SeqNoRepository;
import com.example.project_card.cards.domain.Brand;
import com.example.project_card.cards.domain.Card;
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

    public ReceiveApplyDTO ReceiveApply(ReceiveApplyDTO receiveApplyDTO)
    {
        log.info("{ ApplyService } : ReceiveApply 생성");
        String ssn = receiveApplyDTO.ssn1() + "-" +receiveApplyDTO.ssn2();

        // 불능 체크
        ComCodeDtl comCodeDtl = comCodeDtlRepository.findByGroupCdAndCode("C007", receiveApplyDTO.applClas());
        if(comCodeDtl == null)
            throw BaseException.type(ApplyErrorCode.APPLY_CLAS_NOT_FOUND);

        switch (comCodeDtl.getCodeNm()) {
            case "최초신규" -> {
                // 기존카드 소지 여부
                Long existCard = cardRepository.countBySsn(ssn);
                if (existCard > 0) // 불능코드 04 (기존카드 존재)
                    return SaveReceiveApply(ssn, receiveApplyDTO, "1", "04");
            }
            case "추가신규" -> {
                // 동일 브랜드 카드 소지 여부
                Long existBrdCard = cardRepository.countBySsnAndBrd(ssn, receiveApplyDTO.brd());
                if (existBrdCard > 0) // 불능코드 04 (기존카드 존재)
                    return SaveReceiveApply(ssn, receiveApplyDTO, "1", "04");
            }
            case "재발급" -> {
                // 기존카드 소지 여부 (자유인 체크)
                Long existCard = cardRepository.countBySsn(ssn);
                if (existCard == 0) // 불능코드 05 (기존카드 미존재)
                    return SaveReceiveApply(ssn, receiveApplyDTO, "1", "05");

                // 동일 브랜드 최종카드 소지 여부
                Card lstBrdCard = cardRepository.findBySsnAndBrdAndLstCrdF(ssn, receiveApplyDTO.brd(), "1");
                if (lstBrdCard == null) // 불능코드 05 (기존카드 미존재)
                    return SaveReceiveApply(ssn, receiveApplyDTO, "1", "05");
            }
        }

        // 당일 가입신청 검증
        if(comCodeDtl.getCodeNm().equals("최초신규") || comCodeDtl.getCodeNm().equals("추가신규"))
        {
            ReceiveApply receiveApply = receiveApplyRepository.findBySsnAndRcvD(ssn, String.valueOf(receiveApplyDTO.rcvD()));
            if(receiveApply != null) // 불능코드 01 (당일신청내역 존재)
                return SaveReceiveApply(ssn, receiveApplyDTO, "1", "01");
        }

//        // 결제계좌 검증
//        if(!ValidStlAct(receiveApplyDTO.stlAct())) // 불능코드 02 (결제계좌 오류)
//            return SaveReceiveApply(ssn, receiveApplyDTO, "1", "02");
//
//        // 비밀번호 검증
//        if(!ValidScrtNo(receiveApplyDTO.scrtNo())) // 불능코드 03 (비밀번호 오류)
//            return SaveReceiveApply(ssn, receiveApplyDTO, "1", "03");


        // 정상 등록
        return SaveReceiveApply(ssn, receiveApplyDTO, "", "");
    }

    public String GenerateNoSeq(String now)
    {
        String maxNoSeq = noSeqRepository.findMaxNoSeqByRcvD(now);
        return (maxNoSeq == null) ? "1" : String.valueOf(Integer.parseInt(maxNoSeq) + 1);
    }

    public ReceiveApplyDTO SaveReceiveApply(String ssn, ReceiveApplyDTO receiveApplyDTO, String impsbClas, String impsbCd)
    {
        String crdNo = ""; // 카드 번호

        // 접수일련번호 발번
        String now = LocalDate.now().toString();
        String rcvSeqNo = GenerateNoSeq(now);
        NoSeq noSeq = NoSeq.createNoSeq(
                now,
                rcvSeqNo
        );
        noSeqRepository.save(noSeq);

        // 등록
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
                receiveApplyDTO.emailAdr1()+ "@" + receiveApplyDTO.emailAdr2(),
                crdNo,
                impsbClas,
                impsbCd
        );

        ReceiveApply newReceiveApply = receiveApplyRepository.save(receiveApply);
        ReceiveApplyDTO newReceiveApplyDTO = ReceiveApplyDTO.fromReceiveApply(newReceiveApply);
        log.info("{ ApplyService } : ReceiveApply 생성 완료");

        // 정상 등록시 (고객/결제/카드 테이블 insert 또는 update)
        if(!impsbCd.equals("1"))
            SavePsbReceiveApply(ssn, receiveApplyDTO);

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
        return (maxCrdNo == null) ? "111111111" : String.valueOf(Integer.parseInt(maxCrdNo) + 1);
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

    public void SavePsbReceiveApply(String ssn, ReceiveApplyDTO receiveApplyDTO)
    {
        // 카드번호 발번
        String cN = GenerateCrdNo(); // 일련번호 9자리
        String crdGrd = "11"; // 등급 2자리

        ComCodeDtl comCodeDtl = comCodeDtlRepository.findByGroupCdAndCode("C004", receiveApplyDTO.brd());
        if(comCodeDtl == null)
            throw BaseException.type(ApplyErrorCode.BRD_NOT_FOUND);
        String brd = Brand.valueOf(comCodeDtl.getCodeNm()).getNumber(); // 브랜드 4자리
        String crdNo = checkCrdNo(crdGrd + cN + brd);

        switch (comCodeDtl.getCodeNm()) {
            case "최초신규" -> {
                // 고객번호 발번
                String custNo = GenerateCustNo();

                // 고객 insert
                Customer customer = Customer.createCustomer(
                        custNo,
                        ssn,
                        String.valueOf(receiveApplyDTO.rcvD()),
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
                        receiveApplyDTO.emailAdr1()+ "@" + receiveApplyDTO.emailAdr2()
                );
                billRepository.save(bill);

                // 카드 insert


            }
            case "추가신규" -> {
                // 고객 update

                // 결제 update

                // 카드 insert


            }
            case "재발급" -> {
                // 고객 update

                // 결제 update

                // 기존소지카드 update

                // 신규카드 insert

            }
        }



    }
}
