package com.codementor.apigateway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TokenExceptionAdvice {
    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorDTO> handleUserException(TokenException e) {
        return e.getResponse();
    }
}
