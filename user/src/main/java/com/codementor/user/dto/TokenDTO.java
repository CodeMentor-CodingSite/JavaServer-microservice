package com.codementor.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.ResponseCookie;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
    private String accessToken;
    private String refreshToken;

    public String createAccessTokenCookie() {
        return ResponseCookie.from("access_token", this.accessToken)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .build()
                .toString();
    }

    public String createRefreshTokenCookie() {
        return ResponseCookie.from("refresh_token", this.refreshToken)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .build()
                .toString();
    }
}
