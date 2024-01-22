package com.codementor.user.service;

import com.codementor.user.config.JwtProvider;
import com.codementor.user.dto.TokenDTO;
import com.codementor.user.dto.UserDTO;
import com.codementor.user.dto.UserProfileDTO;
import com.codementor.user.entity.User;
import com.codementor.user.exception.UserErrorEnum;
import com.codementor.user.exception.UserException;
import com.codementor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        checkExistedUser(userDTO);

        User user = UserDTO.toEntity(userDTO).encodePassword(passwordEncoder);

        return UserDTO.toDTO(userRepository.save(user));
    }

    @Transactional
    public TokenDTO doLogin(UserDTO userDTO) {
        User responseUser = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new UserException(UserErrorEnum.INVALID_LOGIN_EMAIL));

        if (!responseUser.isMatchPassword(userDTO.getPassword(), passwordEncoder)) {
            throw new UserException(UserErrorEnum.INVALID_LOGIN_PASSWORD);
        }

        return createAllToken(responseUser);
    }

    @Transactional
    public UserProfileDTO getUser(Long userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorEnum.NOT_FOUND_USER_BY_USER_ID));

        return UserProfileDTO.builder()
                .nickname(foundUser.getNickname())
                .build();
    }

    private void checkExistedUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail()))
            throw new UserException(UserErrorEnum.EXIST_EMAIL);

        if (userRepository.existsByNickname(userDTO.getNickname()))
            throw new UserException(UserErrorEnum.EXIST_NICKNAME);
    }

    private TokenDTO createAllToken(User user) {
        String accessToken = createAccessToken(user);
        String refreshToken = createRefreshToken(user);

        return TokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String createAccessToken(User user) {
        return jwtProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole());
    }

    private String createRefreshToken(User user) {
        return jwtProvider.createRefreshToken(user.getId(), user.getEmail(), user.getRole());
    }
}
