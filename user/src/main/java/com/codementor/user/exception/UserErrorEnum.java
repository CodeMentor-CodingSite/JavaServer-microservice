package com.codementor.user.exception;

import com.codementor.user.dto.ErrorDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorEnum {
    EXIST_EMAIL(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), "해당 이메일은 사용중입니다"),
    EXIST_NICKNAME(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), "해당 닉네임은 사용중입니다"),
    INVALID_LOGIN_EMAIL(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), "해당 이메일 계정이 존재하지 않습니다"),
    INVALID_LOGIN_PASSWORD(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), "비밀번호가 올바르지 않습니다"),
    NOT_FOUND_USER_BY_USER_ID(HttpStatus.NOT_FOUND, HttpStatus.BAD_REQUEST.value(), "사용자 ID로 사용자를 찾을 수 없습니다");

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
