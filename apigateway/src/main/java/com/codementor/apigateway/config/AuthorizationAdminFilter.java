package com.codementor.apigateway.config;

import com.codementor.apigateway.exception.TokenException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.NotAcceptableStatusException;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class AuthorizationAdminFilter extends AbstractGatewayFilterFactory<Object> {
    private final JwtUtil jwtUtil;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String accessToken = jwtUtil.getToken(exchange.getRequest(), "access");
            String refreshToken = jwtUtil.getToken(exchange.getRequest(), "refresh");;

            if (accessToken != null) {
                Claims claims = jwtUtil.parseTokenToClaims(accessToken, "admin");

                if (!jwtUtil.checkAdminRole(claims)) {
                    exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

                    return exchange.getResponse()
                            .writeWith(Flux.just(
                                    exchange.getResponse().bufferFactory().wrap("Access Denied! Access only admin!".getBytes(StandardCharsets.UTF_8))
                            ));
                }

                jwtUtil.addAuthorizationHeaders(exchange.getRequest(), claims);
            } else if (refreshToken != null) {
                Claims claims = jwtUtil.parseTokenToClaims(refreshToken, "admin");

                jwtUtil.addAuthorizationHeaders(exchange.getRequest(), claims);
            }

            return chain.filter(exchange);
        };
    }

    @Bean
    public ErrorWebExceptionHandler tokenValidation() {
        return new JwtErrorHandler();
    }
}