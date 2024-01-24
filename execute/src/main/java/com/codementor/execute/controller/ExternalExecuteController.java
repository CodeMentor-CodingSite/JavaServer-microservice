package com.codementor.execute.controller;

import com.codementor.execute.dto.external.UserQuestionsStatus;
import com.codementor.execute.service.QuestionHelperService;
import lombok.AllArgsConstructor;
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
