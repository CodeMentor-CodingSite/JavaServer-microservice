package com.codementor.question.controller;

import com.codementor.question.dto.external.EvalQuestionRequest;
import com.codementor.question.dto.external.EvaluationDto;
import com.codementor.question.dto.external.QuestionDifficultyCounts;
import com.codementor.question.dto.external.UserSolvedRatioTotalDto;
import com.codementor.question.service.ExecutionHelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequiredArgsConstructor
public class ExternalApiController {

    private final ExecutionHelperService executionHelperService;


    @PostMapping("/api/external/execute")
    public ResponseEntity<EvaluationDto> evalQuestion(EvalQuestionRequest evalQuestionRequest) {
        EvaluationDto evaluationDto = executionHelperService.createEvaluationDto(evalQuestionRequest.getQuestionId(), evalQuestionRequest.getUserLanguage());
        return ResponseEntity.ok(evaluationDto);
    }


    @PostMapping("/api/external/getQuestionsDifficultyCounts")
    public QuestionDifficultyCounts getQuestionsDifficultyCounts(){
        return executionHelperService.getQuestionsDifficultyCounts();
    }

    @PostMapping("/api/external/getUserSolvedCounts")
    public UserSolvedRatioTotalDto getUserSolvedCounts(UserSolvedRatioTotalDto req){
        return executionHelperService.getUserSolvedRatioSubmitDto(req);
    }
}
