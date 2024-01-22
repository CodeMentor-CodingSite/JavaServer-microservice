package com.codementor.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TestController {
    @GetMapping("/api/user/test")
    public String testUser() {
        return "This is user service!";
    }

    @GetMapping("/api/user/check/admin1")
    public String admin1Test(HttpServletRequest request) {
        Long id = Long.valueOf(request.getHeader("id"));
        String email = request.getHeader("email");

        return "Access Permit!\nThis is Admin1 API.\n Your id : " + id + ", email : " + email;
    }

    @GetMapping("/api/user/check/admin2")
    public String admin2Test(HttpServletRequest request) {
        Long id = Long.valueOf(request.getHeader("id"));
        String email = request.getHeader("email");

        return "Access Permit!\nThis is Admin2 API.\n Your id : " + id + ", email : " + email;
    }

    @GetMapping("/api/user/check/user1")
    public String user1Test(HttpServletRequest request) {
        Long id = Long.valueOf(request.getHeader("id"));
        String email = request.getHeader("email");

        return "Access Permit!\nThis is User1 API.\n Your id : " + id + ", email : " + email;
    }

    @GetMapping("/api/user/check/user2")
    public String user2Test(HttpServletRequest request) {
        Long id = Long.valueOf(request.getHeader("id"));
        String email = request.getHeader("email");

        return "Access Permit!\nThis is User2 API.\n Your id : " + id + ", email : " + email;
    }
}