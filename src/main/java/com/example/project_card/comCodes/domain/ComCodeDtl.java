package com.example.project_card.comCodes.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(ComCodeDtlId.class)
public class ComCodeDtl {

    @Id
    private String groupCd;

    private String groupNm;

    @Id
    private String code;

    private String codeNm;

    private String etcCd1;

    private String etcCd2;

    private String stcCd3;

    private String remark;
}
