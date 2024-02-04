package com.codementor.controller;

import com.codementor.core.dto.ResponseDto;
import com.codementor.dto.request.UserCodeExecutionRequest;
import com.codementor.service.SseConnectionService;
import com.codementor.service.UserCodeExecutionRequestProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class ExecuteUsercodeController {

    private final UserCodeExecutionRequestProducer userCodeExecutionRequestProducer;
    private final SseConnectionService sseConnectionService;


    /**
     * 유저와 SseEmitter를 연결한다.
     * @param userId 유저 아이디
     * @return SseEmitter
     */
    @PostMapping("/api/openSseConnection")
    public SseEmitter subscribe(@RequestHeader("userId") Long userId){
        return sseConnectionService.createEmitterForUsers(userId);
    }

    /**
     * subscribe를 통해 SseConnection을 먼저 만들어 놓는다.
     * 유저에게 SseEmitter를 통해 gpt평가 결과를 전송한다.
     * @param userCodeExecutionRequest 유저의 코드 및 관련 데이터
     * @return 성공 메시지
     */
    @PostMapping("/api/execute/userCode")
    public ResponseDto evaluateUserCode(@RequestHeader("userId") Long userId,
                                        @RequestBody UserCodeExecutionRequest userCodeExecutionRequest) {
        userId = 1L; //Todo : remove this line
        userCodeExecutionRequest.setUserId(userId);
        userCodeExecutionRequestProducer.sendUserCodeExecutionRequestToKafka(userCodeExecutionRequest);
        return ResponseDto.ok("user code sent to execution kafka topic");
    }
}
