package com.codementor.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendToKafka {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> KafkaTemplate;

    public void sendData(String topicName, Object data) {
        try {
            String json = objectMapper.writeValueAsString(data);
            KafkaTemplate.send(topicName, json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
