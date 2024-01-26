package com.codementor.question.controller;

import com.codementor.question.dto.response.PlanResponse;
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

    @GetMapping("/question")
    public Page<QuestionDto> getQuestionList(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            Long userId) {
        return questionService.getPaginatedQuestionDtos(userId, PageRequest.of(page, size));
    }

    @GetMapping("/question/{questionId}")
    public QuestionDetailDtoResponse getQuestionDetail(@PathVariable Long questionId) {
        return questionService.getQuestionById(questionId);
    }

    @GetMapping("question/{questionId}/initial-code/{langauage}")
    public QuestionInitCodeResponse getQuestionInitialCode(@PathVariable Long questionId, @PathVariable String language) {
        return questionService.getQuestionInitialCode(questionId, language);
    }

    @GetMapping("/plan")
    public PlanResponse getPlanList() {
        return questionService.getAllPlans();
    }
}
