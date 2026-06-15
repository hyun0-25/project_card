package com.example.project_card.comCodes.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ComCode {

    @Id
    private String groupCd;

    private String groupNm;

    private String etcCd1;

    private String etcCd2;

    private String stcCd3;

    private String remark;

}
