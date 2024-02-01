package com.codementor.apigateway.config;

import com.codementor.apigateway.exception.TokenErrorEnum;
import com.codementor.apigateway.exception.TokenException;
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
            String path = exchange.getRequest().getPath().toString();

            if (accessToken == null) {
                if (refreshToken != null && path.equals("/api/user/reissue")) {
                    jwtUtil.validateToken(refreshToken, exchange, "user");
                    return chain.filter(exchange);
                }
                throw new TokenException(TokenErrorEnum.UNAUTHORIZED);
            }

            jwtUtil.validateToken(accessToken, exchange, "user");

            return chain.filter(exchange);
        };
    }

    @Bean
    public ErrorWebExceptionHandler userTokenValidation() {
        return new JwtErrorHandler();
    }
}