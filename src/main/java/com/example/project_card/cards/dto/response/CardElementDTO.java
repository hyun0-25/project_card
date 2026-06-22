package com.example.project_card.cards.dto.response;

import com.example.project_card.cards.domain.Card;
import lombok.Builder;

@Builder
public record CardElementDTO(
        String crdNo,
        String engNm,
        String vldDur,
        String brd,
        String crdGrd,
        String regD,
        String bfCrdNo,
        String lstCrdF,
        String mgtBbrn
) {
    public static CardElementDTO fromCardElement(Card card)
    {
        return CardElementDTO.builder()
                .crdNo(card.getCrdNo())
                .engNm(card.getEngNm())
                .vldDur(card.getVldDur())
                .brd(card.getBrd())
                .crdGrd(card.getCrdGrd())
                .regD(card.getRegD())
                .bfCrdNo(card.getBfCrdNo())
                .lstCrdF(card.getLstCrdF())
                .mgtBbrn(card.getMgtBbrn())
                .build();
    }
}
