package com.codementor.question.controller;

import com.codementor.question.dto.evaluation.EvalQuestionRequest;
import com.codementor.question.dto.evaluation.EvaluationDto;
import com.codementor.question.service.ExecutionHelperService;
import com.codementor.question.service.GenericSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
}
