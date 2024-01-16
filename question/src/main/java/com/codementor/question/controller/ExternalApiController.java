package com.codementor.question.controller;

import com.codementor.question.dto.evaluation.EvalRequest;
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

    @Value("${server.execute.url}")
    private String executeUrl;
    private final ExecutionHelperService executionHelperService;
    private final GenericSender genericSender;


    @PostMapping("/api/external/execute")
    public ResponseEntity<EvaluationDto> evalQuestion(EvalRequest evalRequest) {
        EvaluationDto evaluationDto = executionHelperService.createEvaluationDto(evalRequest.getQuestionId(), evalRequest.getUserLanguage());
        return ResponseEntity.ok(evaluationDto);
    }
}
