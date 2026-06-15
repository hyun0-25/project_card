package com.example.project_card.cards.domain;

import com.example.project_card.applys.domain.NoSeqId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(CardId.class)
public class Card {

    @Id
    private String crdNo;

    @Id
    private String custNo;

    private String mgtBbrn;

    private String regD;

    private String ssn;

    private String vldDur;

    private String brd;

    private String scrtNo;

    private String engNm;

    private String bfCrdNo;

    private String lstCrdF;

    private String fstRegD;

    private String crdGrd;

}
