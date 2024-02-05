package com.codementor.controller;

import com.codementor.dto.external.UserQuestionsStatus;
import com.codementor.service.QuestionHelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExternalExecuteController {

    private final QuestionHelperService questionHelperService;

    @PostMapping("/api/external/question/get/user/status")
    public UserQuestionsStatus getUserQuestionsStatus(@RequestBody Long userId) {
        return questionHelperService.getUserQuestionsStatus(userId);
    }

    @PostMapping("/api/external/correct-user-question-id-list")
    public List<Long> getCorrectUserQuestionIdList(@RequestBody Long userId) {
        return questionHelperService.getCorrectUserQuestionIdList(userId);
    }
}
