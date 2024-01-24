package com.codementor.user.config;

import com.codementor.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${token.access-token-expiretime}")
    public int ACCESS_TOKEN_EXPIRETIME;

    @Value("${token.refresh-token-expiretime}")
    public int REFRESH_TOKEN_EXPIRETIME;

    private final Key key;

    public JwtProvider(@Value("${token.secret}") String tokenSecret) {
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

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuer("codementor")
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_EXPIRETIME))
                .claim("id", id)
                .claim("email", email)
                .claim("role", role)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseTokenToClaims(String token) {
        token = token.replace("Bearer", "");

        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody();
    }
}
