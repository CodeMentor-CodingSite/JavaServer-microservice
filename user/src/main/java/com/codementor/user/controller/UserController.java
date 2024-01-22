package com.codementor.user.controller;

import com.codementor.user.dto.UserDTO;
import com.codementor.user.dto.TokenDTO;
import com.codementor.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/user/register")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.CREATED);
    }

    @PostMapping("/api/user/login")
    public ResponseEntity<TokenDTO> doLogin(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        TokenDTO tokenDTO = userService.doLogin(userDTO);

        response.addHeader("access_token", tokenDTO.getAccessToken());
        response.addHeader("refresh_token", tokenDTO.getRefreshToken());

        return new ResponseEntity<>(tokenDTO, HttpStatus.CREATED);
    }
}
