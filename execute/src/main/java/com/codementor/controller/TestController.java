package com.codementor.controller;

import com.codementor.dto.evaluation.EvalQuestionRequest;
import com.codementor.dto.evaluation.EvaluationDto;
import com.codementor.dto.request.UserCodeExecutionRequest;
import com.codementor.service.UserCodeExecutionRequestProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    @Value("${server.question.url}")
    private String questionUrl;

    private final UserCodeExecutionRequestProducer userCodeExecutionRequestProducer;

    @PostMapping("/api/execute/test")
    public String evaluateUserCode(@RequestBody UserCodeExecutionRequest userCodeExecutionRequest) {
        System.out.println("sending dto to question server");
        EvaluationDto evaluationDto = userCodeExecutionRequestProducer.getQuestionDataFromQuestionServer(questionUrl+ "/api/external/test", new EvalQuestionRequest());
        System.out.println("received evaluationDto from question server");
        return "user code sent to execution kafka topic";
    }
}
