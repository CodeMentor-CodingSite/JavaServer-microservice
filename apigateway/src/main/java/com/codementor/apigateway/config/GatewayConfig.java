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
                        .path("/api/user/check/user1", "/api/user/check/user2", "/api/user/users", "/api/user/reissue", "/api/user/likes/{segment}", "/api/user/subscribe/{segment}", "/api/user/plan")
                        .filters(f -> f.filter(userFilter.apply(new Object())))
                        .uri(userURI))
                .route("user-service", r -> r
                        .path("/api/user/**")
                        .uri(userURI))
                .route("question-service", r -> r
                        .path("/api/question/questions/{segment}", "/api/question/questions/{segment1}/languages/{segment2}")
                        .uri(questionURI))
                .route("question-user-service", r -> r
                        .path("/api/question/**")
                        .filters(f -> f.filter(userFilter.apply(new Object())))
                        .uri(questionURI))
                .route("execute-service", r -> r
                        .path("/api/execute/{questionId}/history")
                        .uri(executeURI))
                .route("execute-user-service", r -> r
                        .path("/api/execute/**")
                        .filters(f -> f.filter(userFilter.apply(new Object())))
                        .uri(executeURI))
                .build();
    }
}
