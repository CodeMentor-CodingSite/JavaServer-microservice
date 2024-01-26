package com.codementor.apigateway.config;

import com.codementor.apigateway.exception.TokenErrorEnum;
import com.codementor.apigateway.exception.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.security.Key;

@Configuration
public class JwtUtil {
    private final Key key;

    public JwtUtil(@Value("${token.secret}") String tokenSecret) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(tokenSecret));
    }

    public void validateToken(String token, ServerWebExchange exchange, String role) {
        Claims claims = parseTokenToClaims(token);

        if (role.equals("admin")) checkAdminRole(claims);

        addAuthorizationHeaders(exchange.getRequest(), claims);
    }

    private void addAuthorizationHeaders(ServerHttpRequest request, Claims claims) {
        request.mutate()
                .header("id", claims.get("id").toString())
                .header("email", claims.get("email").toString())
                .build();
    }

    private Claims parseTokenToClaims(String token) {
        token = token.replace("Bearer", "");

        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getToken(ServerHttpRequest request, String type) {
        String token = null;

        if (type.equals("access") && request.getHeaders().containsKey("Authorization")) {
            token = request.getHeaders().get("Authorization").get(0);
        } else if (type.equals("refresh") && request.getHeaders().containsKey("refresh_token")) {
            token = request.getHeaders().get("refresh_token").get(0);
        }

        return token;
    }

    private void checkAdminRole(Claims claims) {
        if (!claims.get("role").toString().equals("ADMIN")) throw new TokenException(TokenErrorEnum.NOT_ADMIN_TOKEN);
    }
}
