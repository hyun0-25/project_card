package com.example.project_card.users.dto.response;

import com.example.project_card.users.domain.Bill;
import com.example.project_card.users.domain.Customer;
import lombok.Builder;

@Builder
public record UserDetailDTO(
        String ssn,
        String hgNm,
        String birthD,
        String hdpNo,
        String bnkCd,
        String stlAct,
        String billAdr
) {
    public static UserDetailDTO fromUserDetail(Customer customer, Bill bill)
    {
        return UserDetailDTO.builder()
                .ssn(customer.getSsn())
                .hgNm(customer.getHgNm())
                .birthD(customer.getBirthD())
                .hdpNo(customer.getHdpNo())
                .bnkCd(bill.getBnkCd())
                .stlAct(bill.getStlAct())
                .billAdr(bill.getBillAdr1() == null || bill.getBillAdr1().isEmpty() ? "" : bill.getBillAdr1() +" (" + bill.getBillAdr2() +")")
                .build();
    }
}
