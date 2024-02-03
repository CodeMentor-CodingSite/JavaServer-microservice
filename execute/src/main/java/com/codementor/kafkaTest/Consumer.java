package com.codementor.kafkaTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Consumer {

    private static final String TOPIC_NAME = "test";
    private static final String GROUP_ID = "test";

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = TOPIC_NAME, groupId = GROUP_ID)
    public void kafkaListener(String jsonMessage) {
        System.out.println("listening to kafka");
        try {
            MessageDTO messageDTO = objectMapper.readValue(jsonMessage, MessageDTO.class);
            System.out.println("Consumer: " + messageDTO);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
