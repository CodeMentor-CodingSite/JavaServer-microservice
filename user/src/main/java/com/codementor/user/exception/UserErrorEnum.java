package com.codementor.user.exception;

import com.codementor.user.dto.ErrorDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorEnum {
    EXIST_EMAIL(HttpStatus.BAD_REQUEST, 4001, "해당 이메일은 사용중입니다"),
    EXIST_NICKNAME(HttpStatus.BAD_REQUEST, 4002, "해당 닉네임은 사용중입니다"),
    INVALID_LOGIN_EMAIL(HttpStatus.BAD_REQUEST, 4003, "해당 이메일 계정이 존재하지 않습니다"),
    INVALID_LOGIN_PASSWORD(HttpStatus.BAD_REQUEST, 4004, "비밀번호가 올바르지 않습니다");

    private final HttpStatus status;
    private final int code;
    private final String message;

    public ErrorDTO getErrorDTO(){
        return ErrorDTO.builder()
                .errorCode(code)
                .errorMessage(message)
                .build();
    }
}
