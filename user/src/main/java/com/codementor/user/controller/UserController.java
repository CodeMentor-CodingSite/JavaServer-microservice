package com.codementor.user.controller;

import com.codementor.user.dto.*;
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
    public ResponseEntity<TokenDTO> reissueToken(@RequestHeader("id") Long id) {
        TokenDTO tokenDTO = userService.reissueToken(id);

        return ResponseEntity.ok()
                .header(SET_COOKIE_HEADER, tokenDTO.createAccessTokenCookie())
                .build();
    }

    @DeleteMapping("/users")
    public ResponseEntity<TokenDTO> deleteUser(@RequestHeader("id") Long id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> doLogout(@CookieValue("access_token") String token) {
        String message = userService.doLogout(token);

        return ResponseEntity.ok(message);
    }

    @PutMapping("/users")
    public ResponseEntity<String> updateUser(@RequestBody UserUpdateDTO userUpdateDTO, @RequestHeader("id") Long id) {
        String message = userService.updateUser(userUpdateDTO, id);

        return ResponseEntity.ok(message);
    }
}
