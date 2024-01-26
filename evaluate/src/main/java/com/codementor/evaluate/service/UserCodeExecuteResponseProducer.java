package com.codementor.evaluate.service;

import com.codementor.evaluate.dto.EvaluationDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * 카프카에 Producer 역할을 하는 서비스
 */
@Service
@RequiredArgsConstructor
public class UserCodeExecuteResponseProducer {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC_NAME = "usercode.response.topic.v1";
    private static final String GROUP_ID = "usercode.response.group.v1";

    /**
     * Kafka 에 유저코드 실행 결과와 gpt평가 결과값이 포함된 EvaluationDTO 전송
     * @param evaluationResult
     */
    public void sendToKafka(EvaluationDto evaluationResult) {
        System.out.println("Producer: " + evaluationResult);
        try{
            String jsonInString = objectMapper.writeValueAsString(evaluationResult);
            kafkaTemplate.send(TOPIC_NAME, jsonInString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
