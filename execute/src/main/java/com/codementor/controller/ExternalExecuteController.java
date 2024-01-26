package com.codementor.controller;

import com.codementor.dto.external.UserQuestionsStatus;
import com.codementor.service.QuestionHelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExternalExecuteController {

    private final QuestionHelperService questionHelperService;

    @PostMapping("/api/execute/api/external/question/get/user/status")
    public UserQuestionsStatus getUserQuestionsStatus(Long userId) {
        return questionHelperService.getUserQuestionsStatus(userId);
    }
}
