package com.example.project_card.cards.domain;

import com.example.project_card.applys.domain.NoSeqId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(CardId.class)
public class Card {

    @Id
    private String crdNo;

    @Id
    private String custNo;

    private String mgtBbrn;

    private String regD;

    private String ssn;

    private String vldDur;

    private String brd;

    private String scrtNo;

    private String engNm;

    private String bfCrdNo;

    private String lstCrdF;

    private String fstRegD;

    private String crdGrd;

    private Card(String crdNo, String custNo, String mgtBbrn, String regD, String ssn, String vldDur, String brd, String scrtNo, String engNm, String bfCrdNo, String lstCrdF, String fstRegD, String crdGrd) {
        this.crdNo = crdNo;
        this.custNo = custNo;
        this.mgtBbrn = mgtBbrn;
        this.regD = regD;
        this.ssn = ssn;
        this.vldDur = vldDur;
        this.brd = brd;
        this.scrtNo = scrtNo;
        this.engNm = engNm;
        this.bfCrdNo = bfCrdNo;
        this.lstCrdF = lstCrdF;
        this.fstRegD = fstRegD;
        this.crdGrd = crdGrd;
    }

    public static Card createCard(String crdNo, String custNo, String mgtBbrn, String regD, String ssn, String vldDur, String brd, String scrtNo, String engNm, String bfCrdNo, String lstCrdF, String fstRegD, String crdGrd) {
        return new Card(crdNo, custNo, mgtBbrn, regD, ssn, vldDur, brd, scrtNo, engNm, bfCrdNo, lstCrdF, fstRegD, crdGrd);
    }

    public void updateLstCrdF()
    {
        this.lstCrdF = "";
    }
}