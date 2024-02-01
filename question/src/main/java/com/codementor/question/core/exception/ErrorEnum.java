package com.codementor.question.core.exception;

import com.codementor.question.core.dto.ErrorDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorEnum {
    // 인증 관련 : 4010
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, 4010, "Invalid access token"),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, 4011, "Expired access token"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, 4012, "Invalid refresh token"),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, 4013, "Expired refresh token"),

    // DB 관련 : 5000
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5000, "Database error"),
    RECORD_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, 5001, "Record not found"),

    // 서버 통신 관련 : 5030
    API_SERVER_COMMUNICATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5030, "API Server communication error"),
    QUESTION_SERVER_COMMUNICATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5031, "Question Server communication error"),
    EXECUTE_SERVER_COMMUNICATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5032, "Execute Server communication error"),
    USER_SERVER_COMMUNICATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5033, "User Server communication error"),
    EVALUATE_SERVER_COMMUNICATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5034, "Evaluate Server communication error"),

    // Kafka 관련 : 5060
    KAFKA_PRODUCER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5060, "Kafka producer error"),
    KAFKA_CONSUMER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5061, "Kafka consumer error"),

    // AI 관련 : 5090
    GPT_ERROR(HttpStatus.GATEWAY_TIMEOUT, 5090, "GPT error");

    private final HttpStatus httpStatus;
    private final int errorCode;
    private final String message;

    public ErrorDto getErrorDto() {
        return ErrorDto.builder()
                .errorCode(errorCode)
                .errorMessage(message)
                .build();
    }

    public ErrorDto getErrorDto(String message) {
        return ErrorDto.builder()
                .errorCode(errorCode)
                .errorMessage(message)
                .build();
    }
}