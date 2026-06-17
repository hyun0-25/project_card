package com.example.project_card.cards.exception;

import com.example.project_card.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CardErrorCode implements ErrorCode {
    CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "CARD_001", "카드 정보가 존재하지 않습니다")
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
