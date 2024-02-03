package com.codementor.kafkaTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Producer {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC_NAME = "test";
    private static final String GROUP_ID = "test";

    public void sendToKafka(MessageDTO messageDTO) {
        System.out.println("Producer: " + messageDTO);
        try{
            String jsonInString = objectMapper.writeValueAsString(messageDTO);
            kafkaTemplate.send(TOPIC_NAME, jsonInString);
            System.out.println("Message sent to Kafka");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
