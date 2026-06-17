package com.example.project_card.applys.exception;

import com.example.project_card.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReceiveApplyErrorCode implements ErrorCode {
    APPLY_CLAS_NOT_FOUND(HttpStatus.NOT_FOUND, "APPLY_001", "신청 구분 코드가 존재하지 않습니다"),
    BRD_NOT_FOUND(HttpStatus.NOT_FOUND, "APPLY_002", "브랜드 코드가 존재하지 않습니다"),
    RECEIVE_APPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "APPLY_003", "입회 신청 정보가 존재하지 않습니다"),
    RECEIVE_APPLY_CANNOT_MODIFIED(HttpStatus.BAD_REQUEST, "APPLY_004", "입회 신청 정보를 수정할 수 없습니다 (카드 발급완료)"),
    ;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
