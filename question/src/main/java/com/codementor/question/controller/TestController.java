package com.codementor.question.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/api/question/test")
    public String testQuestion() {
        return "This is question service!";
    }
}
