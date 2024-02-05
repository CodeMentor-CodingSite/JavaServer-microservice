package com.codementor.evaluate.service;

import com.codementor.evaluate.dto.EvaluationDto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Kafka 에서 메시지를 받아서 테스트 코드들에 대한 결과값과 Gpt 서버의 평가 결과를 받아서 다시 kafka 에 전송하는 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserCodeExecuteRequestConsumer {

    private final EvaluationService evaluationService;
    private final ChatService chatService;
    private final UserCodeExecuteResponseProducer userCodeExecuteResponseProducer;

    private static final String TOPIC_NAME = "usercode.request.topic.v1";

    private static final String GROUP_ID = "usercode.request.group.v1";

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * Kafka 에서 메시지를 받아서 테스트 코드들에 대한 결과값과 Gpt 서버의 평가 결과를 받아서 다시 kafka 에 전송
     * @param jsonMessage EvaluationDto 객체를 json 으로 변환한 문자열
     */
    @KafkaListener(topics = TOPIC_NAME, groupId = GROUP_ID)
    public void recordListener(String jsonMessage) {
        System.out.println("running recordListener");
        try{
            System.out.println("kafka message = " + jsonMessage);
            EvaluationDto evaluationDto = objectMapper.readValue(jsonMessage, EvaluationDto.class);
            log.info("recordListener message = {}",jsonMessage);

            // 비동기통신으로 코드 실행 결과값들을 Kafka 에 전송
            sendToKafkaWithExecutionResults(evaluationDto);

            // 비동기통신으로 Gpt 서버의 평가 결과를 Kafka 에 전송
            sendToKafkaWithGptEvaluation(evaluationDto);

        } catch (Exception e) {
            log.error("recordListener ERROR message = {}",jsonMessage, e);
        }
    }

    /**
     * 비동기통신으로 코드 실행 결과값들을 Kafka 에 전송
     * @param evaluationDto Kafka 에서 받은 EvaluationDto 객체
     */
    @Async
    protected void sendToKafkaWithExecutionResults(EvaluationDto evaluationDto){
        Long startTime = System.nanoTime();
        // 코드 실행에 대한 결과 값들이 담긴 배열
        ArrayList<String> executionResults = evaluationService.processExecutionResults(evaluationDto);
        Long durationInMillis = (System.nanoTime() - startTime) / 1000000;
        // 코드 실행에 대한 결과 값들을 기존 EvaluationDto에 담음
        for (int i = 0; i < executionResults.size(); i++) {
            evaluationDto.getTestCaseDtoList().get(i).setTestCaseResult(executionResults.get(i));
        }

        evaluationDto.setTestCaseResults(executionResults);
        evaluationDto.setExecuteTime(durationInMillis);
        // Kafka 에 결과값 전송
        userCodeExecuteResponseProducer.sendToKafka(evaluationDto);
    }

    /**
     * 비동기통신으로 Gpt 서버의 평가 결과를 Kafka 에 전송
     * @param evaluationDto Kafka 에서 받은 EvaluationDto 객체
     * @throws Exception Gpt 서버와 통신 중 에러 발생
     */
    @Async
    protected void sendToKafkaWithGptEvaluation(EvaluationDto evaluationDto) throws Exception {
        // Gpt 서버의 평가 결과
        String evaluationResult = chatService.gptUserCodeEvaluation(evaluationDto);
        // Gpt 서버의 평가 결과를 기존 EvaluationDto에 담음
        evaluationDto.setGptEvaluation(evaluationResult);
        // Kafka 에 결과값 전송
        userCodeExecuteResponseProducer.sendToKafka(evaluationDto);
    }
}
