package com.example.project_card.applys.exception;

import com.example.project_card.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApplyErrorCode implements ErrorCode {
    APPLY_CLAS_NOT_FOUND(HttpStatus.NOT_FOUND, "APPLY_001", "신청 구분 코드가 존재하지 않습니다"),
    BRD_NOT_FOUND(HttpStatus.NOT_FOUND, "APPLY_002", "브랜드 코드가 존재하지 않습니다")
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
