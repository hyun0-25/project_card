package com.example.project_card.applys.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class ReceiveApplyId {

    private String ssn;

    private String rcvD;

    private String rcvSeqNo;
}
