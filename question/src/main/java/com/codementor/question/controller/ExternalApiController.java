package com.codementor.question.controller;

import com.codementor.question.dto.external.*;
import com.codementor.question.service.ExecutionHelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
@RequiredArgsConstructor
public class ExternalApiController {

    private final ExecutionHelperService executionHelperService;


    @PostMapping("/api/external/execute")
    public EvaluationDto evalQuestion(@RequestBody EvalQuestionRequest evalQuestionRequest) {
        System.out.println("EvaluationDto received");
        System.out.println(evalQuestionRequest.toString());
        EvaluationDto evaluationDto = executionHelperService.createEvaluationDto(
                evalQuestionRequest.getQuestionId(),
                evalQuestionRequest.getUserLanguage());
        return evaluationDto;
    }


    @PostMapping("/api/external/getQuestionsDifficultyCounts")
    public QuestionDifficultyCounts getQuestionsDifficultyCounts() {
        System.out.println("QuestionDifficultyCounts received");
        return executionHelperService.getAllQuestionsDifficultyCounts();
    }

    @PostMapping("/api/external/getUserSolvedCounts")
    public UserSolvedRatioTotalDto getUserSolvedCounts(@RequestBody UserSolvedRatioTotalDto req) {
        System.out.println("UserSolvedRatioTotalDto received");
        return executionHelperService.getUserSolvedRatioSubmitDto(req);
    }

    @PostMapping("/api/external/getUserSolvedCategory")
    public UserSolvedCategoryDtoList getUserSolvedCategory(@RequestBody UserSolvedQuestionIdList req) {
        return executionHelperService.getUserSolvedCategoryQuestionList(req);
    }

    @PostMapping("/api/external/getQuestionTitleAndDifficultyFromId")
    public List<UserSolvedQuestionIdAndTitleAndTimeResponse> getQuestionNameFromId(@RequestBody List<UserSolvedQuestionIdAndTitleAndTimeResponse> req) {
        return executionHelperService.getQuestionNameFromId(req);
    }
}
