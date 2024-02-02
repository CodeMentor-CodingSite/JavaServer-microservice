package com.codementor.apigateway.config;

import com.codementor.apigateway.exception.TokenErrorEnum;
import com.codementor.apigateway.exception.TokenException;
import com.codementor.apigateway.service.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.security.Key;

@Configuration
@RequiredArgsConstructor
public class JwtUtil {

    private final RedisService redisService;

    private final Key key;

    @Autowired
    public JwtUtil(RedisService redisService, @Value("${token.secret}") String tokenSecret) {
        this.redisService = redisService;
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(tokenSecret));
    }

    public void validateToken(String token, ServerWebExchange exchange, String role) {
        Claims claims = parseTokenToClaims(token);
        String id = claims.get("id").toString();
        String value = redisService.getValue(id);
        if (redisService.checkExistsValue(value)) throw new TokenException(TokenErrorEnum.WRONG_TOKEN);

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

        if (type.equals("access") && request.getCookies().containsKey("access_token")) {
            token = request.getCookies().get("access_token").get(0).getValue();
        } else if (type.equals("refresh") && request.getCookies().containsKey("refresh_token")) {
            token = request.getCookies().get("refresh_token").get(0).getValue();
        }

        return token;
    }

    private void checkAdminRole(Claims claims) {
        if (!claims.get("role").toString().equals("ADMIN")) throw new TokenException(TokenErrorEnum.NOT_ADMIN_TOKEN);
    }
}
