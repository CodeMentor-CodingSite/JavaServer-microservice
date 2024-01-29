package com.codementor.user.controller;

import com.codementor.user.dto.TokenDTO;
import com.codementor.user.dto.UserDTO;
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
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> doLogin(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        TokenDTO tokenDTO = userService.doLogin(userDTO);

        response.addHeader("access_token", tokenDTO.getAccessToken());
        response.addHeader("refresh_token", tokenDTO.getRefreshToken());

        return new ResponseEntity<>(tokenDTO, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<UserProfileDTO> getUser(@RequestHeader("id") Long id) {
        UserProfileDTO userProfileDTO = userService.getUser(id);

        return new ResponseEntity<>(userProfileDTO, HttpStatus.OK);
    }

    @GetMapping("/reissue")
    public ResponseEntity<TokenDTO> reissueToken(@RequestHeader("refresh_token") String token) {
        TokenDTO tokenDTO = userService.reissueToken(token);

        return new ResponseEntity<>(tokenDTO, HttpStatus.OK);
    }

    @DeleteMapping("/users")
    public ResponseEntity<TokenDTO> deleteUser(@RequestHeader("id") Long id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
