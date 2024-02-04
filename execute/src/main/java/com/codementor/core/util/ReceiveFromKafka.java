package com.codementor.core.util;

import com.codementor.core.exception.CodeMentorException;
import com.codementor.core.exception.ErrorEnum;
import com.codementor.dto.evaluation.EvaluationDto;
import com.codementor.service.UserCodeExecutionResponseConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReceiveFromKafka {

    private static final String TOPIC_NAME = "usercode.response.topic.v1";
    private static final String GROUP_ID = "usercode.response.group.v1";

    private final ObjectMapper objectMapper;

    private final UserCodeExecutionResponseConsumer userCodeExecutionResponseConsumer;

    @KafkaListener(topics = TOPIC_NAME, groupId = GROUP_ID)
    public void userCodeResultListener(String jsonMessage) {
        try {
            EvaluationDto evaluationDto = objectMapper.readValue(jsonMessage, EvaluationDto.class);
            System.out.println("Consumer: " + evaluationDto.toString());
            userCodeExecutionResponseConsumer.receivedEvaluationResult(evaluationDto);
        } catch (Exception e) {
            throw new CodeMentorException(ErrorEnum.KAFKA_CONSUMER_ERROR);
        }
    }
}
