package com.codementor.user.controller;

import com.codementor.user.dto.TokenDTO;
import com.codementor.user.dto.UserCreateDTO;
import com.codementor.user.dto.UserLoginDTO;
import com.codementor.user.dto.UserProfileDTO;
import com.codementor.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private static final String SET_COOKIE_HEADER = "Set-Cookie";

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        String message = userService.createUser(userCreateDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> doLogin(@RequestBody UserLoginDTO userLoginDTO) {
        TokenDTO tokenDTO = userService.doLogin(userLoginDTO);

        return ResponseEntity.ok()
                .header(SET_COOKIE_HEADER, tokenDTO.createAccessTokenCookie())
                .header(SET_COOKIE_HEADER, tokenDTO.createRefreshTokenCookie())
                .build();
    }

    @GetMapping("/users")
    public ResponseEntity<UserProfileDTO> getUser(@RequestHeader("id") Long id) {
        UserProfileDTO userProfileDTO = userService.getUser(id);

        return ResponseEntity.ok(userProfileDTO);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDTO> reissueToken(@RequestHeader("refresh_token") String token) {
        TokenDTO tokenDTO = userService.reissueToken(token);

        return new ResponseEntity<>(tokenDTO, HttpStatus.OK);
    }

    @DeleteMapping("/users")
    public ResponseEntity<TokenDTO> deleteUser(@RequestHeader("id") Long id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> doLogout(@RequestHeader("Authorization") String token) {
        String message = userService.doLogout(token);

        return ResponseEntity.ok(message);
    }
}
