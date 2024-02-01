package com.codementor.user.config;

import com.codementor.user.entity.UserRole;
import com.codementor.user.service.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {


    @Value("${token.access-token-expiretime}")
    public int ACCESS_TOKEN_EXPIRETIME;

    @Value("${token.refresh-token-expiretime}")
    public int REFRESH_TOKEN_EXPIRETIME;

    private final Key key;

    private final RedisService redisService;


    @Autowired
    public JwtProvider(RedisService redisService, @Value("${token.secret}") String tokenSecret) {
        this.redisService = redisService;
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(tokenSecret));
    }

    public String createAccessToken(Long id, String email, UserRole role) {
        Date date = new Date();

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuer("codementor")
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_EXPIRETIME))
                .claim("id", id)
                .claim("email", email)
                .claim("role", role)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Long id, String email, UserRole role) {
        Date date = new Date();

        String refreshToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuer("codementor")
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_EXPIRETIME))
                .claim("id", id)
                .claim("email", email)
                .claim("role", role)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        redisService.setValue(String.valueOf(id), refreshToken, Duration.ofMillis(REFRESH_TOKEN_EXPIRETIME));

        return refreshToken;
    }

    public Claims parseTokenToClaims(String token) {
        token = token.replace("Bearer", "");

        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long validateAccessToken(String token) {
        Claims claims = parseTokenToClaims(token);
        String id = claims.get("id").toString();

        return Long.valueOf(id);
    }

    public void deleteRefreshToken(Long id) {
        redisService.deleteValues(String.valueOf(id));
    }

    public void setBlackListAccessToken(Long id, String accessToken) {
        long expiredAccessTokenTime = getExpiredTime(accessToken).getTime() - new Date().getTime();
        redisService.setValue("blackList" + id, accessToken, Duration.ofMillis(expiredAccessTokenTime));
    }

    private Date getExpiredTime(String accessToken) {
        Claims claims = parseTokenToClaims(accessToken);
        return claims.getExpiration();
    }
}

