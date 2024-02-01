package com.codementor.apigateway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TokenErrorEnum {
    NOT_ADMIN_TOKEN(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value(), "접근할 수 없습니다. Admin 사용자가 아닙니다"),
    NOT_USER_TOKEN(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value(), "접근할 수 없습니다. User 사용자가 아닙니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "토큰이 필요합니다"),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "토큰이 만료되었습니다."),
    WRONG_TOKEN(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "잘못된 토큰입니다.");


    private final HttpStatus status;
    private final int code;
    private final String message;

    public ErrorDTO getErrorDTO() {
        return ErrorDTO.builder()
                .errorCode(code)
                .errorMessage(message)
                .build();
    }
}
