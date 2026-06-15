package com.example.project_card.users.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bill {

    private String stlAct;

    private String bnkCd;

    private String dpsNm;

    private String stlMtd;

    private String stlDd;

    @Id
    private String custNo;

    private String prcsClas;

    private String stmtSndMtd;

    private String stmtDeniClas;

    private String billZip;

    private String billAdr1;

    private String billAdr2;

    private String emailAdr;

    private Bill(String stlAct, String bnkCd, String dpsNm, String stlMtd, String stlDd, String custNo, String prcsClas, String stmtSndMtd, String stmtDeniClas, String billZip, String billAdr1, String billAdr2, String emailAdr)
    {
        this.stlAct = stlAct;
        this.bnkCd = bnkCd;
        this.dpsNm = dpsNm;
        this.stlMtd = stlMtd;
        this.stlDd = stlDd;
        this.custNo = custNo;
        this.prcsClas = prcsClas;
        this.stmtSndMtd = stmtSndMtd;
        this.stmtDeniClas = stmtDeniClas;
        this.billZip = billZip;
        this.billAdr1 = billAdr1;
        this.billAdr2 = billAdr2;
        this.emailAdr = emailAdr;
    }

    public static Bill createBill(String stlAct, String bnkCd, String dpsNm, String stlMtd, String stlDd, String custNo, String prcsClas, String stmtSndMtd, String stmtDeniClas, String billZip, String billAdr1, String billAdr2, String emailAdr)
    {
        return new Bill(stlAct, bnkCd, dpsNm, stlMtd, stlDd, custNo, prcsClas, stmtSndMtd, stmtDeniClas, billZip, billAdr1, billAdr2, emailAdr);
    }

}
