package com.example.project_card.cards.service;

import com.example.project_card.cards.domain.Card;
import com.example.project_card.cards.dto.response.CardDetailDTO;
import com.example.project_card.cards.dto.response.CardElementDTO;
import com.example.project_card.cards.dto.response.CardListDTO;
import com.example.project_card.cards.exception.CardErrorCode;
import com.example.project_card.cards.repository.CardRepository;
import com.example.project_card.comCodes.domain.ComCodeDtl;
import com.example.project_card.comCodes.repository.ComCodeDtlRepository;
import com.example.project_card.global.exception.BaseException;
import com.example.project_card.users.domain.Bill;
import com.example.project_card.users.domain.Customer;
import com.example.project_card.users.exception.BillErrorCode;
import com.example.project_card.users.exception.CustomerErrorCode;
import com.example.project_card.users.repository.BillRepository;
import com.example.project_card.users.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CustomerRepository customerRepository;
    private final BillRepository billRepository;
    private final ComCodeDtlRepository comCodeDtlRepository;

    public CardListDTO SelectCardList(String ssn1, String ssn2, String crdNo)
    {
        log.info("{ CardService } : SelectCardList 조회");
        String ssn = ssn1 + "-" + ssn2;
        Customer customer = customerRepository.findBySsn(ssn);
        if(customer == null)
            throw BaseException.type(CustomerErrorCode.CUSTOMER_NOT_FOUND);

        Bill bill = billRepository.findByCustNo(customer.getCustNo());
        if(bill == null)
            throw BaseException.type(BillErrorCode.BILL_NOT_FOUND);
        // 결제방법
        ComCodeDtl comCodeDtl = comCodeDtlRepository.findByGroupCdAndCode("C002", bill.getStlMtd());
        String stlMtd = comCodeDtl.getCodeNm();
        // 결제일자
        comCodeDtl = comCodeDtlRepository.findByGroupCdAndCode("C009", bill.getStlDd());
        String stlDd = comCodeDtl.getCodeNm();
        // 결제은행
        String bnkCd = "";
        if(bill.getBnkCd() != null && !bill.getBnkCd().equals(""))
        {
            comCodeDtl = comCodeDtlRepository.findByGroupCdAndCode("C003", bill.getBnkCd());
            bnkCd = comCodeDtl.getCodeNm();
        }
        // 청구서 발송방법
        comCodeDtl = comCodeDtlRepository.findByGroupCdAndCode("C006", bill.getStmtSndMtd());
        String stmtSndMtd = comCodeDtl.getCodeNm();

        List<CardElementDTO> cardElementDTOList = new ArrayList<>();
        List<Card> cardList = cardRepository.findAllBySsnAndCrdNo(ssn, crdNo);
        if(cardList == null || cardList.size() == 0)
            throw BaseException.type(CardErrorCode.CARD_NOT_FOUND);

        for(Card card: cardList)
        {
            // 브랜드
            comCodeDtl = comCodeDtlRepository.findByGroupCdAndCode("C004", card.getBrd());
            String brd = comCodeDtl.getCodeNm();
            // 등급
            comCodeDtl = comCodeDtlRepository.findByGroupCdAndCode("C005", card.getCrdGrd());
            String crdGrd = comCodeDtl.getCodeNm();
            cardElementDTOList.add(CardElementDTO.fromCardElement(card, brd, crdGrd));
        }
        CardListDTO cardListDTO = CardListDTO.fromCardList(ssn1, ssn2, crdNo, customer, bill, stlMtd, stlDd, bnkCd, stmtSndMtd, cardElementDTOList);
        log.info("{ CardService } : SelectCardList 조회 완료");
        return cardListDTO;
    }

    public CardDetailDTO SelectCardDetail(String ssn1, String ssn2, String crdNo)
    {
        log.info("{ CardService } : SelectCardDetail 조회");
        String ssn = ssn1 + "-" + ssn2;
        Customer customer = customerRepository.findBySsn(ssn);
        if(customer == null)
            throw BaseException.type(CustomerErrorCode.CUSTOMER_NOT_FOUND);

        Card card = cardRepository.findByCrdNoAndCustNo(crdNo, customer.getCustNo());
        if(card == null)
            throw BaseException.type(CardErrorCode.CARD_NOT_FOUND);
        // 브랜드
        ComCodeDtl comCodeDtl = comCodeDtlRepository.findByGroupCdAndCode("C004", card.getBrd());
        String brd = comCodeDtl.getCodeNm();
        // 등급
        comCodeDtl = comCodeDtlRepository.findByGroupCdAndCode("C005", card.getCrdGrd());
        String crdGrd = comCodeDtl.getCodeNm();

        CardDetailDTO cardDetailDTO = CardDetailDTO.fromCardDetail(ssn1, ssn2, crdNo, customer, card, brd, crdGrd);
        log.info("{ CardService } : SelectCardDetail 조회");
        return cardDetailDTO;
    }
}
