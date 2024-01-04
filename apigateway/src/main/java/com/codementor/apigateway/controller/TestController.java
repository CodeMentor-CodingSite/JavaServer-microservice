package com.codementor.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/api/apigateway/test")
    public String test() {
        return "This is apigateway service!!";
    }
}
