package com.example.project_card.cards.domain;

import lombok.Getter;

public enum Brand {
    MASTER("5310"),
    VISA("4906"),
    JCB("3560");

    @Getter
    private final String number;

    Brand(String number) {
        this.number = number;
    }
}
