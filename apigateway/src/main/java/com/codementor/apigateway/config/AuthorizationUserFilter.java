package com.codementor.apigateway.config;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorizationUserFilter extends AbstractGatewayFilterFactory<Object> {
    private final JwtUtil jwtUtil;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String accessToken = jwtUtil.getToken(exchange.getRequest(), "access");
            String refreshToken = jwtUtil.getToken(exchange.getRequest(), "refresh");

            if (accessToken != null) {
                Claims claims = jwtUtil.parseTokenToClaims(accessToken, "user");

                jwtUtil.addAuthorizationHeaders(exchange.getRequest(), claims);
            } else if (refreshToken != null) {
                Claims claims = jwtUtil.parseTokenToClaims(refreshToken, "user");

                jwtUtil.addAuthorizationHeaders(exchange.getRequest(), claims);
            }

            return chain.filter(exchange);
        };
    }

    @Bean
    public ErrorWebExceptionHandler userTokenValidation() {
        return new JwtErrorHandler();
    }
}