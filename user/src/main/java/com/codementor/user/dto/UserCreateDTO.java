package com.codementor.user.dto;

import com.codementor.user.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {

    private String email;

    private String password;

    private String nickname;

    public static User toEntity(UserCreateDTO userCreateDTO) {
        return User.builder()
                .email(userCreateDTO.getEmail())
                .password(userCreateDTO.getPassword())
                .nickname(userCreateDTO.getNickname())
                .build();
    }
}
