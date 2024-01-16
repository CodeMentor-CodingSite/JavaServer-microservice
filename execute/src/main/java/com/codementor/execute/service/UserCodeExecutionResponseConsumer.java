package com.codementor.execute.service;

import com.codementor.execute.kafkaTest.MessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCodeExecutionResponseConsumer {

    private static final String TOPIC_NAME = "usercode.response.topic.v1";
    private static final String GROUP_ID = "usercode.response.group.v1";

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = TOPIC_NAME, groupId = GROUP_ID)
    public void kafkaListener(String jsonMNessage) {
        try {
            MessageDTO messageDTO = objectMapper.readValue(jsonMNessage, MessageDTO.class);
            System.out.println("Consumer: " + messageDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
