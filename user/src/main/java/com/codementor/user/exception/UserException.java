package com.codementor.user.exception;

import com.codementor.user.dto.ErrorDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class UserException extends RuntimeException{
    private final UserErrorEnum userErrorEnum;

    public ResponseEntity<ErrorDTO> getResponse() {
        return new ResponseEntity<>(
                userErrorEnum.getErrorDTO(),
                userErrorEnum.getStatus()
        );
    }
}
