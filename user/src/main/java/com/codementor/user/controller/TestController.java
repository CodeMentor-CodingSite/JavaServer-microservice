package com.codementor.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/api/user/test")
    public String testUser() {
        return "This is user service!";
    }
}