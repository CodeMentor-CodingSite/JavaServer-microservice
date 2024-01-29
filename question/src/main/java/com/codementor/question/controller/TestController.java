package com.codementor.question.controller;

import com.codementor.question.dto.external.EvaluationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/api/question/test")
    public String testQuestion() {
        return "This is question service!";
    }

    @PostMapping("/api/external/test")
    public ResponseEntity<EvaluationDto> testExternal() {
        System.out.println("EvaluationDto received");
        System.out.println("Sending back EvaluationDto");
        return new ResponseEntity<>(new EvaluationDto(), null, 200);
    }
}
