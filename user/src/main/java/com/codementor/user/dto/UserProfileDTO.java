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
    private static final String DEFAULT_IMAGE_URL = "https://file.notion.so/f/f/fc7a0770-8294-4680-9cb3-c81efe407127/f4236f2e-f620-4a91-b0ac-39da39691b6f/b73a2bde5327ac18b2ee2c5e0f7f27d3.jpg?id=7cb08601-ef23-4e60-bd86-adac4ca9f14a&table=block&spaceId=fc7a0770-8294-4680-9cb3-c81efe407127&expirationTimestamp=1706616000000&signature=sDQZ0sZPC7nKw568Q0epvqojVmgk8ZB4I1t8guVC6v0&downloadName=b73a2bde5327ac18b2ee2c5e0f7f27d3.jpg";

    private String nickname;
    @Builder.Default
    private String imageUrl = DEFAULT_IMAGE_URL;
}
