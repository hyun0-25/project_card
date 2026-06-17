package com.example.project_card.applys.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(ReceiveApplyId.class)
public class ReceiveApply {

    @Id
    private String ssn;

    @Id
    private String rcvD;

    @Id
    private String rcvSeqNo;

    private String applD;

    private String birthD;

    private String hgNm;

    private String engNm;

    private String stlMtd;

    private String stlAct;

    private String bnkCd;

    private String stlDd;

    private String mgtBbrn;

    private String applClas;

    private String stmtSndMtd;

    private String billAdr1;

    private String billAdr2;

    private String billZip;

    private String hdpNo;

    private String brd;

    private String scrtNo;

    private String emailAdr;

    private String crdNo;

    private String impsbClas;

    private String impsbCd;

    private ReceiveApply(String ssn, String rcvD, String rcvSeqNo, String applD, String birthD, String hgNm, String engNm, String stlMtd, String stlAct, String bnkCd, String stlDd, String mgtBbrn, String applClas, String stmtSndMtd, String billAdr1, String billAdr2, String billZip, String hdpNo, String brd, String scrtNo, String emailAdr, String crdNo, String impsbClas, String impsbCd) {
        this.ssn = ssn;
        this.rcvD = rcvD;
        this.rcvSeqNo = rcvSeqNo;
        this.applD = applD;
        this.birthD = birthD;
        this.hgNm = hgNm;
        this.engNm = engNm;
        this.stlMtd = stlMtd;
        this.stlAct = stlAct;
        this.bnkCd = bnkCd;
        this.stlDd = stlDd;
        this.mgtBbrn = mgtBbrn;
        this.applClas = applClas;
        this.stmtSndMtd = stmtSndMtd;
        this.billAdr1 = billAdr1;
        this.billAdr2 = billAdr2;
        this.billZip = billZip;
        this.hdpNo = hdpNo;
        this.brd = brd;
        this.scrtNo = scrtNo;
        this.emailAdr = emailAdr;
        this.crdNo = crdNo;
        this.impsbClas = impsbClas;
        this.impsbCd = impsbCd;
    }

    public static ReceiveApply createReceiveApply(String ssn, String rcvD, String rcvSeqNo, String applD, String birthD, String hgNm, String engNm, String stlMtd, String stlAct, String bnkCd, String stlDd, String mgtBbrn, String applClas, String stmtSndMtd, String billAdr1, String billAdr2, String billZip, String hdpNo, String brd, String scrtNo, String emailAdr, String crdNo, String impsbClas, String impsbCd) {
        return new ReceiveApply(ssn, rcvD, rcvSeqNo, applD, birthD, hgNm, engNm, stlMtd, stlAct, bnkCd, stlDd, mgtBbrn, applClas, stmtSndMtd, billAdr1, billAdr2, billZip, hdpNo, brd, scrtNo, emailAdr, crdNo, impsbClas, impsbCd);
    }

    public void updateReceiveApplyCrdNo(String crdNo)
    {
        this.crdNo = crdNo;
    }
}