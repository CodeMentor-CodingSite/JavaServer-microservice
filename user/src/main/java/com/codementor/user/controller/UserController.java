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

import javax.servlet.http.HttpServletResponse;

@RestController()
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        String message = userService.createUser(userCreateDTO);

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> doLogin(@RequestBody UserLoginDTO userLoginDTO, HttpServletResponse response) {
        TokenDTO tokenDTO = userService.doLogin(userLoginDTO);

        response.addHeader("access_token", tokenDTO.getAccessToken());
        response.addHeader("refresh_token", tokenDTO.getRefreshToken());

        return new ResponseEntity<>(tokenDTO, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<UserProfileDTO> getUser(@RequestHeader("id") Long id) {
        UserProfileDTO userProfileDTO = userService.getUser(id);

        return new ResponseEntity<>(userProfileDTO, HttpStatus.OK);
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

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
}
