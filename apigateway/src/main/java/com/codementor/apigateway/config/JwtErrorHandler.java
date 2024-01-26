package com.codementor.apigateway.config;

import com.codementor.apigateway.exception.TokenErrorEnum;
import com.codementor.apigateway.exception.TokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class JwtErrorHandler implements ErrorWebExceptionHandler {
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal Server Error";
    public static final String NULL_POINTER_ERROR_MESSAGE = "Null Pointer Exception";
    public static final String TOKEN_EXPIRED_ERROR_MESSAGE = "Token has expired";
    public static final String JWT_SIGNATURE_DOES_NOT_MATCH = "JWT signature does not match";


    private static String getErrorCode(int errorCode, String errorMessage) {
        return "{\"errorCode\":" + errorCode + ", \"errorMessage\":\"" + errorMessage + "\"}";
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        int errorCode = 500;
        String errorMessage = INTERNAL_SERVER_ERROR_MESSAGE;
        if (ex instanceof NullPointerException) {
            errorCode = 400;
            errorMessage = NULL_POINTER_ERROR_MESSAGE;
        } else if (ex instanceof ExpiredJwtException) {
            errorCode = 401;
            errorMessage = TOKEN_EXPIRED_ERROR_MESSAGE;
        } else if (ex instanceof SignatureException) {
            errorCode = 401;
            errorMessage = JWT_SIGNATURE_DOES_NOT_MATCH;
        } else if (ex instanceof TokenException) {
            TokenErrorEnum tokenErrorEnum = ((TokenException) ex).getTokenErrorEnum();
            errorCode = tokenErrorEnum.getCode();
            errorMessage = tokenErrorEnum.getMessage();
        }

        byte[] bytes = getErrorCode(errorCode, errorMessage).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);

        exchange.getResponse().getHeaders().set("Content-Type", "application/json");
        exchange.getResponse().setStatusCode(HttpStatus.valueOf(errorCode));
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }
}
