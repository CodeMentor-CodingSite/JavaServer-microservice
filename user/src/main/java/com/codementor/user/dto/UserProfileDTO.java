package com.codementor.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private static final String DEFAULT_IMAGE_URL = "https://url.kr/qghxmf";

    private String nickname;
    @Builder.Default
    private String imageUrl = DEFAULT_IMAGE_URL;
}
