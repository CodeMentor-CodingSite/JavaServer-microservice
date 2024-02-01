package com.codementor.core.exception;

import com.codementor.core.dto.ErrorDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class CodeMentorException extends RuntimeException {

    private final ErrorEnum errorEnum;

    public ResponseEntity<ErrorDto> getResponse() {
        return new ResponseEntity<>(
                errorEnum.getErrorDto(),
                errorEnum.getHttpStatus()
        );
    }

    public ResponseEntity<ErrorDto> getResponse(String message) {
        return new ResponseEntity<>(
                errorEnum.getErrorDto(message),
                errorEnum.getHttpStatus()
        );
    }
}