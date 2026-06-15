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
@IdClass(NoSeqId.class)
public class NoSeq {

    @Id
    private String rcvD;

    @Id
    private String rcvSeqNo;

    private NoSeq(String rcvD, String rcvSeqNo) {
        this.rcvD = rcvD;
        this.rcvSeqNo = rcvSeqNo;
    }

    public static NoSeq createNoSeq(String rcvD, String rcvSeqNo){
        return new NoSeq(rcvD, rcvSeqNo);
    }
}
