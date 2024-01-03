package com.codementor.execute.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/api/execute/test")
    public String testExecute() {
        return "This is execute service!";
    }
}
