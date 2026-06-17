package com.example.project_card.users.exception;

import com.example.project_card.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomerErrorCode implements ErrorCode {
    CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND, "CUSTOMER_1", "고객 정보가 존재하지 않습니다")
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
