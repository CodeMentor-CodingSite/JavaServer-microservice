package com.codementor.user.service;

import com.codementor.user.config.JwtProvider;
import com.codementor.user.dto.TokenDTO;
import com.codementor.user.dto.UserCreateDTO;
import com.codementor.user.dto.UserLoginDTO;
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
    public String createUser(UserCreateDTO userCreateDTO) {
        checkExistedUser(userCreateDTO);

        User user = UserCreateDTO.toEntity(userCreateDTO).encodePassword(passwordEncoder);

        userRepository.save(user);

        return "Success";
    }

    @Transactional
    public TokenDTO doLogin(UserLoginDTO userLoginDTO) {
        User responseUser = userRepository.findByEmail(userLoginDTO.getEmail())
                .orElseThrow(() -> new UserException(UserErrorEnum.INVALID_LOGIN_EMAIL));

        if (!responseUser.isMatchPassword(userLoginDTO.getPassword(), passwordEncoder)) {
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

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public TokenDTO reissueToken(String token) {
        Long id = jwtProvider.validateRefreshToken(token);
        User foundUser = userRepository.findById(id)
                .orElseThrow(() -> new UserException(UserErrorEnum.NOT_FOUND_USER_BY_USER_ID));

        return TokenDTO.builder()
                .accessToken(createAccessToken(foundUser))
                .build();
    }

    @Transactional
    public String doLogout(String token) {
        Long id = jwtProvider.validateAccessToken(token);
        jwtProvider.deleteRefreshToken(id);
        jwtProvider.setBlackListAccessToken(id, token);

        return "Success";
    }

    private void checkExistedUser(UserCreateDTO userCreateDTO) {
        if (userRepository.existsByEmail(userCreateDTO.getEmail()))
            throw new UserException(UserErrorEnum.EXIST_EMAIL);

        if (userRepository.existsByNickname(userCreateDTO.getNickname()))
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
