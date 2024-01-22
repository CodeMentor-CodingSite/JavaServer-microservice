package com.codementor.apigateway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class TokenException extends RuntimeException{
    private final TokenErrorEnum tokenErrorEnum;

    public ResponseEntity<ErrorDTO> getResponse() {
        return new ResponseEntity<>(
                tokenErrorEnum.getErrorDTO(),
                tokenErrorEnum.getStatus()
        );
    }
}
