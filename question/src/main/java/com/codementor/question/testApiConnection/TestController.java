package com.codementor.question.testApiConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {

    @PostMapping("/api/question/test")
    public ReceiveDto sendToQuestion(@RequestBody SendDto sendDto) {
        System.out.println("");
        System.out.println("received dto from execute server");
        System.out.println(sendDto.getUsername());
        System.out.println("sending dto to question server");
        System.out.println("");
        return new ReceiveDto("test name");
    }
}
