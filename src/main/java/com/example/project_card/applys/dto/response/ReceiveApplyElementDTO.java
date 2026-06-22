package com.example.project_card.applys.dto.response;

import com.example.project_card.applys.domain.ReceiveApply;
import lombok.Builder;

@Builder
public record ReceiveApplyElementDTO(
        String rcvD,
        String rcvSeqNo,
        String ssn,
        String hgNm,
        String engNm,
        String applClas,
        String brd,
        String hdpNo,
        String impsbClas,
        String impsbCd
) {
    public static ReceiveApplyElementDTO fromReceiveApplyElement(ReceiveApply receiveApply, String applClasNm, String codeNm)
    {
        return ReceiveApplyElementDTO.builder()
                .rcvD(receiveApply.getRcvD())
                .rcvSeqNo(receiveApply.getRcvSeqNo())
                .ssn(receiveApply.getSsn())
                .hgNm(receiveApply.getHgNm())
                .engNm(receiveApply.getEngNm())
                .applClas(applClasNm)
                .brd(receiveApply.getBrd())
                .hdpNo(receiveApply.getHdpNo())
                .impsbClas(receiveApply.getImpsbClas() != null && receiveApply.getImpsbClas().equals("1") ? "불능" : "정상")
                .impsbCd(codeNm)
                .build();
    }

}
