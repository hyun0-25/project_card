package com.example.project_card.users.domain;

import com.example.project_card.cards.domain.CardId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(CustomerId.class)
public class Customer {

    @Id
    private String custNo;

    @Id
    private String ssn;

    private String regD;

    private String hgNm;

    private String birthD;

    private String hdpNo;

    private Customer(String custNo, String ssn, String regD, String hgNm, String birthD, String hdpNo)
    {
        this.custNo = custNo;
        this.ssn = ssn;
        this.regD = regD;
        this.hgNm = hgNm;
        this.birthD = birthD;
        this.hdpNo = hdpNo;
    }

    public static Customer createCustomer(String custNo, String ssn, String regD, String hgNm, String birthD, String hdpNo)
    {
        return new Customer(custNo, ssn, regD, hgNm, birthD, hdpNo);
    }

    public void updateCustomer(String hgNm, String birthD, String hdpNo)
    {
        this.hgNm = hgNm;
        this.birthD = birthD;
        this.hdpNo = hdpNo;
    }
}
