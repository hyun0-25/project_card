package com.example.project_card.cards.dto.response;

import com.example.project_card.cards.domain.Card;
import com.example.project_card.users.domain.Customer;
import lombok.Builder;

@Builder
public record CardDetailDTO(
        String ssn1,
        String ssn2,
        String crdNo,
        String hgNm,
        String engNm,
        String regD,
        String vldDur,
        String brd,
        String crdGrd,
        String bfCrdNo,
        String custNo,
        String mgtBbrn,
        String lstCrdF,
        String fstRegD
) {
    public static CardDetailDTO EmptyCardDetailDTO(String ssn1, String ssn2, String crdNo)
    {
        return new CardDetailDTO(ssn1, ssn2, crdNo, "", "", "", "", "", "", "", "", "", "", "");
    }
    public static CardDetailDTO fromCardDetail(String ssn1, String ssn2, String crdNo, Customer customer, Card card, String brd, String crdGrd)
    {
        return CardDetailDTO.builder()
                .ssn1(ssn1)
                .ssn2(ssn2)
                .crdNo(crdNo)
                .hgNm(customer.getHgNm())
                .engNm(card.getEngNm())
                .regD(card.getRegD())
                .vldDur(card.getVldDur())
                .brd(brd)
                .crdGrd(crdGrd)
                .bfCrdNo(card.getBfCrdNo())
                .custNo(card.getCustNo())
                .mgtBbrn(card.getMgtBbrn())
                .lstCrdF(card.getLstCrdF() != null && card.getLstCrdF().equals("1") ? "YES" : "NO")
                .fstRegD(card.getFstRegD())
                .build();
    }
}
