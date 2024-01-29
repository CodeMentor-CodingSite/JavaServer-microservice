package com.codementor.user.dto;

import com.codementor.user.entity.User;
import com.codementor.user.entity.UserRole;
import com.codementor.user.entity.UserStatus;
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

    private String firstname;

    private String lastname;

    private UserRole role;

    private UserStatus status;

    public static User toEntity(UserCreateDTO userCreateDTO) {
        return User.builder()
                .email(userCreateDTO.getEmail())
                .password(userCreateDTO.getPassword())
                .nickname(userCreateDTO.getNickname())
                .firstname(userCreateDTO.getFirstname())
                .lastname(userCreateDTO.getLastname())
                .role(userCreateDTO.getRole())
                .build();
    }
}
