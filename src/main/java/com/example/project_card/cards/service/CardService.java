package com.example.project_card.cards.service;

import com.example.project_card.cards.domain.Card;
import com.example.project_card.cards.dto.response.CardElementDTO;
import com.example.project_card.cards.dto.response.CardListDTO;
import com.example.project_card.cards.exception.CardErrorCode;
import com.example.project_card.cards.repository.CardRepository;
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

        List<CardElementDTO> cardElementDTOList = new ArrayList<>();
        List<Card> cardList = cardRepository.findAllBySsnAndCrdNo(ssn, crdNo);
        if(cardList == null || cardList.size() == 0)
            throw BaseException.type(CardErrorCode.CARD_NOT_FOUND);

        for(Card card: cardList)
        {
            cardElementDTOList.add(CardElementDTO.fromCardElement(card));
        }
        CardListDTO cardListDTO = CardListDTO.fromCardList(ssn1, ssn2, crdNo, customer, bill, cardElementDTOList);
        log.info("{ CardService } : SelectCardList 조회 완료");
        return cardListDTO;
    }


}
