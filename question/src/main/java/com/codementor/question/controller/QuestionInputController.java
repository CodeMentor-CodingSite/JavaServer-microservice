package com.codementor.question.controller;

import com.codementor.question.core.dto.ResponseDto;
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
    public ResponseDto<String> questionInput(@RequestBody QuestionInputRequest request) {
        Long questionId = questionInputService.questionInput(request);
        return ResponseDto.ok(questionId.toString());
    }

    @PostMapping("/testcase")
    public ResponseDto<String> testCaseInput(@RequestBody TestCaseRequest request) {
        Long testCaseId = questionInputService.testCaseInput(request);
        return ResponseDto.ok(testCaseId.toString());
    }

    @PostMapping("/converter")
    public ResponseDto<String> converterInput(@RequestBody ConverterInputRequest request) {
        Long converterId = questionInputService.converterInput(request);
        return ResponseDto.ok(converterId.toString());
    }

    @PostMapping("/question-code")
    public ResponseDto<String> questionCodeInput(@RequestBody QuestionCodeInputRequest request) {
        Long questionCodeId = questionInputService.questionCodeInput(request);
        return ResponseDto.ok(questionCodeId.toString());
    }
}
