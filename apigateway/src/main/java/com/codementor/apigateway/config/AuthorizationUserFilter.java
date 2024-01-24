package com.codementor.apigateway.config;

import com.codementor.apigateway.exception.TokenErrorEnum;
import com.codementor.apigateway.exception.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class AuthorizationUserFilter extends AbstractGatewayFilterFactory<Object> {
    private final JwtUtil jwtUtil;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String accessToken = jwtUtil.getToken(exchange.getRequest(), "access");
            String refreshToken = jwtUtil.getToken(exchange.getRequest(), "refresh");

            if (accessToken == null || refreshToken == null) throw new TokenException(TokenErrorEnum.UNAUTHORIZED);

            if (!validateToken(refreshToken, exchange)) throw new TokenException(TokenErrorEnum.EXPIRED_JWT);

            if (!validateToken(accessToken, exchange)) return redirectReissue(exchange);

            return chain.filter(exchange);
        };
    }

    private boolean validateToken(String token, ServerWebExchange exchange) {
        try {
            Claims claims = jwtUtil.parseTokenToClaims(token, "user");

            jwtUtil.addAuthorizationHeaders(exchange.getRequest(), claims);

            return true;
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    private Mono<Void> redirectReissue(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        response.getHeaders().setLocation(URI.create("/api/user/reissue"));
        return response.setComplete();
    }

    @Bean
    public ErrorWebExceptionHandler userTokenValidation() {
        return new JwtErrorHandler();
    }
}