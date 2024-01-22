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
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String firstname;
    private String lastname;
    private UserRole role;
    private UserStatus status;

    public static User toEntity(UserDTO userDTO) {
        return User.builder()
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .nickname(userDTO.getNickname())
                .firstname(userDTO.getFirstname())
                .lastname(userDTO.getLastname())
                .role(userDTO.getRole())
                .build();
    }

    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }
}
