package com.codementor.execute.controller;

import com.codementor.execute.dto.ResponseDTO;
import com.codementor.evaluate.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GptController {

    private final ChatService chatService;


    /**
     * GPT-3와의 연동을 확인하는 임시 api
     * @param message : 사용자가 입력한 메시지
     * @return : GPT-3의 응답
     * @throws Exception : 예외처리
     */
    @PostMapping("/api/execute/gptevaluation")
    public ResponseDTO gptEvaluation(@RequestBody String message) throws Exception{
        String gptEvaluation = chatService.davinciResponse(message);
        return new ResponseDTO(200, true, gptEvaluation, "success");
    }

}
