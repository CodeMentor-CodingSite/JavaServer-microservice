package com.codementor.question.controller;

import com.codementor.question.dto.request.ConverterInputRequest;
import com.codementor.question.dto.request.QuestionCodeInputRequest;
import com.codementor.question.dto.request.QuestionInputRequest;
import com.codementor.question.dto.request.TestCaseRequest;
import com.codementor.question.service.QuestionInputService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question/input/")
public class QuestionInputController {

    private final QuestionInputService questionInputService;

    @PostMapping("/question")
    public ResponseEntity<String> questionInput(@RequestBody QuestionInputRequest request) {
        Integer questionId = questionInputService.questionInput(request);
        return ResponseEntity.ok(questionId.toString());
    }

    @PostMapping("/testcase")
    public ResponseEntity<String> testCaseInput(@RequestBody TestCaseRequest request) {
        Integer testCaseId = questionInputService.testCaseInput(request);
        return ResponseEntity.ok(testCaseId.toString());
    }

    @PostMapping("/converter")
    public ResponseEntity<String> converterInput(@RequestBody ConverterInputRequest request) {
        Integer converterId = questionInputService.converterInput(request);
        return ResponseEntity.ok(converterId.toString());
    }

    @PostMapping("/question-code")
    public ResponseEntity<String> questionCodeInput(@RequestBody QuestionCodeInputRequest request) {
        Integer questionCodeId = questionInputService.questionCodeInput(request);
        return ResponseEntity.ok(questionCodeId.toString());
    }
}
