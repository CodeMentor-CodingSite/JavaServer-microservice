package com.codementor.testApiConnection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class TestController {

    @Value("${server.question.url}")
    private String questionUrl;
    private RestTemplate restTemplate;

    @PostMapping("/api/execute/send-to-question")
    public ReceiveDto sendToQuestion(@RequestBody SendDto sendDto) {
        String url = questionUrl + "/api/question/test";
        System.out.println("");
        System.out.println("sending dto to question server");
        ReceiveDto receive = restTemplate.postForObject(url, sendDto, ReceiveDto.class);
        System.out.println("received dto from question server");
        System.out.println(receive.getUsername());
        System.out.println("");
        return receive;
    }

    @GetMapping("/api/execute/send-to-question")
    public String test(){
        System.out.println("test");
        return "test";
    }
}
