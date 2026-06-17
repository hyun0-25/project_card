package com.example.project_card.applys.exception;

import com.example.project_card.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BillErrorCode implements ErrorCode {
    BILL_NOT_FOUND(HttpStatus.NOT_FOUND, "BILL_001", "결제 정보가 존재하지 않습니다")
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
