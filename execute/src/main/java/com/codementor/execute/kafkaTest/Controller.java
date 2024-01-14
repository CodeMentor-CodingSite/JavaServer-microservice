package com.codementor.execute.kafkaTest;

import com.codementor.execute.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final Producer producer;

    @GetMapping("/kafkatest")
    public void test() {
        MessageDTO messageDTO = new MessageDTO("userId", "message");
        System.out.println("Controller: " + messageDTO);
        producer.sendToKafka(messageDTO);
    }
}
