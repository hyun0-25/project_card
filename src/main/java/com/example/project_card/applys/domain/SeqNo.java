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
@IdClass(SeqNoId.class)
public class SeqNo {

    @Id
    private String custNo;

    @Id
    private String crdNo;

    private SeqNo(String custNo, String crdNo)
    {
        this.custNo = custNo;
        this.crdNo = crdNo;
    }

    public static SeqNo createSeqNo(String custNo, String crdNo)
    {
        return new SeqNo(custNo, crdNo);
    }
}
