package com.example.project_card.cards.dto.response;

import com.example.project_card.users.domain.Bill;
import com.example.project_card.users.domain.Customer;
import lombok.Builder;

import java.util.List;

@Builder
public record CardListDTO(
        String ssn1,
        String ssn2,
        String crdNo,
        String custNo,
        String hgNm,
        String hdpNo,
        String regD,
        String stlMtd,
        String bnkCd,
        String stlAct,
        String stlDd,
        String stmtSndMtd,
        String emailAdr,
        String billZip,
        String billAdr1,
        String billAdr2,
        List<CardElementDTO> cardElementDTOList
) {
    public static CardListDTO EmptyCardListDTO(String ssn1, String ssn2, String crdNo)
    {
        return new CardListDTO(ssn1, ssn2, crdNo, "", "", "", "", "", "", "", "", "", "", "", "", "", null);
    }

    public static CardListDTO fromCardList(String ssn1, String ssn2, String crdNo, Customer customer, Bill bill, String stlMtd, String stlDd, String bnkCd, String stmtSndMtd, List<CardElementDTO> cardElementDTOList)
    {
        return CardListDTO.builder()
                .ssn1(ssn1)
                .ssn2(ssn2)
                .crdNo(crdNo)
                .custNo(customer.getCustNo())
                .hgNm(customer.getHgNm())
                .hdpNo(customer.getHdpNo())
                .regD(customer.getRegD())
                .stlMtd(stlMtd)
                .bnkCd(bnkCd)
                .stlAct(bill.getStlAct())
                .stlDd(stlDd)
                .stmtSndMtd(stmtSndMtd)
                .emailAdr(bill.getEmailAdr())
                .billZip(bill.getBillZip())
                .billAdr1(bill.getBillAdr1())
                .billAdr2(bill.getBillAdr2())
                .cardElementDTOList(cardElementDTOList)
                .build();
    }
}
