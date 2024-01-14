package com.codementor.execute.kafkaTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Consumer {

    private static final String TOPIC_NAME = "test.topic.v1";
    private static final String GROUP_ID = "test.group.v1";

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
