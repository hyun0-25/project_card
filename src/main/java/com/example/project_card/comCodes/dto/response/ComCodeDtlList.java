package com.example.project_card.comCodes.dto.response;

import com.example.project_card.comCodes.domain.ComCodeDtl;
import lombok.Builder;

@Builder
public record ComCodeDtlList(
        String groupCd,
        String groupNm,
        String code,
        String codeNm
) {
    public static ComCodeDtlList fromComCodeDetailList(ComCodeDtl comCodeDtl)
    {
        return ComCodeDtlList.builder()
                .groupCd(comCodeDtl.getGroupCd())
                .groupNm(comCodeDtl.getGroupNm())
                .code(comCodeDtl.getCode())
                .codeNm(comCodeDtl.getCodeNm())
                .build();
    }

}
