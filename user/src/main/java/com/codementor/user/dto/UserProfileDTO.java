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
    private static final String DEFAULT_IMAGE_URL = "https://coffeeeeeeee-bucket.s3.ap-northeast-2.amazonaws.com/review-images/b73a2bde5327ac18b2ee2c5e0f7f27d3-1706623598709.jpg";

    private String nickname;

    @Builder.Default
    private String imageUrl = DEFAULT_IMAGE_URL;
}
