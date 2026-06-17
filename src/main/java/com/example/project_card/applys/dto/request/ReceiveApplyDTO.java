package com.example.project_card.applys.dto.request;

import com.example.project_card.applys.domain.ReceiveApply;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ReceiveApplyDTO(
        String ssn1,
        String ssn2,
        LocalDate rcvD,
        String rcvSeqNo,
        String applD,
        String birthD,
        String hgNm,
        String engNm,
        String stlMtd,
        String stlAct,
        String bnkCd,
        String stlDd,
        String applClas,
        String prcsClas,
        String stmtSndMtd,
        String billAdr1,
        String billAdr2,
        String billZip,
        String hdpNo,
        String brd,
        String scrtNo,
        String emailAdr1,
        String emailAdr2,
        String crdNo,
        String impsbCd,
        String impsbCdNm
) {
    public static ReceiveApplyDTO EmptyReceiveApplyInfo(String ssn1, String ssn2)
    {
        return new ReceiveApplyDTO(ssn1, ssn2, LocalDate.now(), "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
    }

    public static ReceiveApplyDTO fromReceiveApply(ReceiveApply receiveApply, String codeNm)
    {
        return ReceiveApplyDTO.builder()
                .ssn1(receiveApply.getSsn().split("-")[0])
                .ssn2(receiveApply.getSsn().split("-")[1])
                .rcvD(LocalDate.parse(receiveApply.getRcvD()))
                .rcvSeqNo(receiveApply.getRcvSeqNo())
                .applD(receiveApply.getApplD())
                .birthD(receiveApply.getBirthD())
                .hgNm(receiveApply.getHgNm())
                .engNm(receiveApply.getEngNm())
                .stlMtd(receiveApply.getStlMtd())
                .stlAct(receiveApply.getStlAct())
                .bnkCd(receiveApply.getBnkCd())
                .stlDd(receiveApply.getStlDd())
                .applClas(receiveApply.getApplClas())
                .prcsClas("1")
                .stmtSndMtd(receiveApply.getStmtSndMtd())
                .billAdr1(receiveApply.getBillAdr1())
                .billAdr2(receiveApply.getBillAdr2())
                .billZip(receiveApply.getBillZip())
                .hdpNo(receiveApply.getHdpNo())
                .brd(receiveApply.getBrd())
                .scrtNo(receiveApply.getScrtNo())
                .emailAdr1(receiveApply.getEmailAdr() == null ? "" : receiveApply.getEmailAdr().split("@")[0])
                .emailAdr2(receiveApply.getEmailAdr() == null  ? "" : receiveApply.getEmailAdr().split("@")[1])
                .crdNo(receiveApply.getCrdNo())
                .impsbCd(receiveApply.getImpsbCd())
                .impsbCdNm(receiveApply.getImpsbCd() == null  ? "정상 등록" : "불능 - " + codeNm)
                .build();
    }
}
