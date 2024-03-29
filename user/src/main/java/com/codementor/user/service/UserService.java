package com.codementor.user.service;

import com.codementor.user.config.JwtProvider;
import com.codementor.user.core.exception.CodeMentorException;
import com.codementor.user.core.exception.ErrorEnum;
import com.codementor.user.dto.*;
import com.codementor.user.entity.User;
import com.codementor.user.entity.UserLike;
import com.codementor.user.repository.userlike.UserLikeRepositorySupport;
import com.codementor.user.repository.user.UserRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepositorySupport userRepositorySupport;
    private final UserLikeRepositorySupport userLikeRepositorySupport;

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;


    @Transactional
    public String createUser(UserCreateDTO userCreateDTO) {
        checkExistedUser(userCreateDTO);

        User user = UserCreateDTO.toEntity(userCreateDTO).encodePassword(passwordEncoder);

        userRepositorySupport.save(user);

        return "Success";
    }

    @Transactional
    public TokenDTO doLogin(UserLoginDTO userLoginDTO) {
        User responseUser = userRepositorySupport.findByEmail(userLoginDTO.getEmail())
                .orElseThrow(() -> new CodeMentorException(ErrorEnum.INVALID_LOGIN_EMAIL));

        if (!responseUser.isMatchPassword(userLoginDTO.getPassword(), passwordEncoder)) {
            throw new CodeMentorException(ErrorEnum.INVALID_LOGIN_PASSWORD);
        }

        return createAllToken(responseUser);
    }

    @Transactional
    public UserProfileDTO getUser(Long userId) {
        User foundUser = userRepositorySupport.findById(userId)
                .orElseThrow(() -> new CodeMentorException(ErrorEnum.NOT_FOUND_USER_BY_USER_ID));

        return UserProfileDTO.builder()
                .nickname(foundUser.getNickname())
                .build();
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepositorySupport.deleteById(userId);
    }

    @Transactional
    public TokenDTO reissueToken(Long id) {
        User foundUser = userRepositorySupport.findById(id)
                .orElseThrow(() -> new CodeMentorException(ErrorEnum.NOT_FOUND_USER_BY_USER_ID));

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

    @Transactional
    public String updateUser(UserUpdateDTO userUpdateDTO, Long id) {
        User foundUser = userRepositorySupport.findById(id)
                .orElseThrow(() -> new CodeMentorException(ErrorEnum.NOT_FOUND_USER_BY_USER_ID));

        String nickname = userUpdateDTO.getNickname();
        checkExistedNickname(nickname);
        if (nickname != null) foundUser.updateNickname(nickname);

        String password = userUpdateDTO.getPassword();
        if (password != null) changePassword(password, foundUser);


        return "Success";
    }

    public String toggleLike(Long questionId, Long userId) {
        User foundUser = userRepositorySupport.findById(userId)
                .orElseThrow(() -> new CodeMentorException(ErrorEnum.NOT_FOUND_USER_BY_USER_ID));

        Optional<UserLike> userLike = userLikeRepositorySupport.findOneByQuestionIdAndUserId(questionId, foundUser.getId());

        if (userLike.isEmpty()) {
            userLikeRepositorySupport.save(UserLike.builder()
                    .questionId(questionId)
                    .user(foundUser)
                    .build());
        } else {
            userLikeRepositorySupport.delete(userLike.get());
        }

        return "Success";
    }

    private void changePassword(String password, User foundUser) {
        foundUser.updatePassword(password);
        foundUser.encodePassword(passwordEncoder);
    }

    private void checkExistedUser(UserCreateDTO userCreateDTO) {
        checkExistedEmail(userCreateDTO.getEmail());
        checkExistedNickname(userCreateDTO.getNickname());
    }

    private void checkExistedEmail(String email) {
        if (userRepositorySupport.existsByEmail(email))
            throw new CodeMentorException(ErrorEnum.EXIST_EMAIL);
    }

    private void checkExistedNickname(String nickname) {
        if (userRepositorySupport.existsByNickname(nickname))
            throw new CodeMentorException(ErrorEnum.EXIST_NICKNAME);
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
