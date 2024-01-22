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

import java.security.Key;

@Configuration
public class JwtUtil {
    private final Key key;

    public JwtUtil(@Value("${token.secret}") String tokenSecret) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(tokenSecret));
    }

    public void addAuthorizationHeaders(ServerHttpRequest request, Claims claims) {
        System.out.println(claims.toString());
        System.out.println(claims.get("id").toString());

        request.mutate()
                .header("id", claims.get("id").toString())
                .header("email", claims.get("email").toString())
                .build();
    }

    public Claims parseTokenToClaims(String token, String role) {
        token = token.substring("Bearer ".length());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody();

        if (role.equals("admin") && !checkAdminRole(claims)) {
            throw new TokenException(TokenErrorEnum.NOT_ADMIN_TOKEN);
        }

        return claims;
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

    public boolean checkAdminRole(Claims claims) {
        return claims.get("role").toString().equals("ADMIN");
    }
}
