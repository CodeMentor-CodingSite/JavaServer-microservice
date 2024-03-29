package com.codementor.question.controller;

import com.codementor.question.core.dto.ResponseDto;
import com.codementor.question.dto.response.QuestionDetailDtoResponse;
import com.codementor.question.dto.QuestionDto;
import com.codementor.question.dto.response.QuestionInitCodeResponse;
import com.codementor.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/questions")
    public Page<QuestionDto> getQuestionList(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestHeader("id") Long userId) {
        return questionService.getPaginatedQuestionDtos(userId, PageRequest.of(page, size));
    }

    @GetMapping("/questions/{questionId}")
    public ResponseDto<QuestionDetailDtoResponse> getQuestionDetail(@PathVariable Long questionId) {
        return ResponseDto.ok( questionService.getQuestionById(questionId));
    }

    @GetMapping("/questions/{questionId}/languages/{language}")
    public ResponseDto<QuestionInitCodeResponse> getQuestionInitialCode(@PathVariable Long questionId, @PathVariable String language) {
        return ResponseDto.ok(questionService.getQuestionInitialCode(questionId, language));
    }
}
