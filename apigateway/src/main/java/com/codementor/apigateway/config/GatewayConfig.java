package com.codementor.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {
    @Value("${uri.user}")
    private String userURI;

    @Value("${uri.question}")
    private String questionURI;

    @Value("${uri.execute}")
    private String executeURI;

    private final AuthorizationAdminFilter adminFilter;
    private final AuthorizationUserFilter userFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-admin-service", r -> r
                        .path("/api/user/check/admin1", "/api/user/check/admin2")
                        .filters(f -> f.filter(adminFilter.apply(new Object())))
                        .uri(userURI))
                .route("user-user-service", r -> r
                        .path("/api/user/check/user1", "/api/user/check/user2", "/api/user/users")
                        .filters(f -> f.filter(userFilter.apply(new Object())))
                        .uri(userURI))
                .route("user-service", r -> r
                        .path("/api/user/**")
                        .uri(userURI))
                .route("question-service", r -> r
                        .path("/api/question/**")
                        .uri(questionURI))
                .route("execute-service", r -> r
                        .path("/api/execute/**")
                        .uri(executeURI))
                .build();
    }
}