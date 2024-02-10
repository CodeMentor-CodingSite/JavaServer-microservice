package com.codementor.user.core.exception;

import com.codementor.user.core.dto.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(CodeMentorException.class)
    public ResponseEntity<ErrorDto> handleException(CodeMentorException e) {
        return e.getResponse();
    }
}
