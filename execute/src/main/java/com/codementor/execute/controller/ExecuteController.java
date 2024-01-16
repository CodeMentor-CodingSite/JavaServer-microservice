package com.codementor.execute.controller;

import com.codementor.execute.dto.request.UserCodeExecutionRequest;
import com.codementor.execute.service.UserCodeExecutionRequestProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
public class ExecuteController {

    private final UserCodeExecutionRequestProducer userCodeExecutionRequestProducer;

    @PostMapping("/api/execute/userCode")
    public String gptEvaluation(@RequestBody UserCodeExecutionRequest userCodeExecutionRequest) {
        userCodeExecutionRequestProducer.sendUserCodeExecutionRequestToKafka(userCodeExecutionRequest);
        return "user code sent to execution kafka topic";
    }
}
