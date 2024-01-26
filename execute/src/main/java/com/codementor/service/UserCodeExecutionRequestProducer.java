package com.codementor.service;

import com.codementor.dto.evaluation.EvalQuestionRequest;
import com.codementor.dto.evaluation.EvaluationDto;
import com.codementor.dto.request.UserCodeExecutionRequest;
import com.codementor.entity.ExecuteUsercode;
import com.codementor.repository.ExecuteUsercodeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserCodeExecutionRequestProducer {

    @Value("${server.question.url}")
    private String questionUrl;


    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ExecuteUsercodeRepository executeUsercodeRepository;
    private RestTemplate restTemplate;

    private static final String TOPIC_NAME = "usercode.request.topic.v1";

    private static final String GROUP_ID = "usercode.request.group.v1";


    /**
     * 유저의 코드 실행 요청을 카프카로 보낸다.
     * @param userCodeExecutionRequest 유저의 코드 실행 및 문제와 관련된 데이터 객체
     */
    public void sendUserCodeExecutionRequestToKafka(UserCodeExecutionRequest userCodeExecutionRequest) {
        EvaluationDto evaluationDto = createEvaluationDto(userCodeExecutionRequest);
        sendToKafka(evaluationDto);
    }

    private EvaluationDto createEvaluationDto(UserCodeExecutionRequest userCodeExecutionRequest) {
        EvalQuestionRequest questionRequest = EvalQuestionRequest.builder()
                .questionId(userCodeExecutionRequest.getQuestionId())
                .userLanguage(userCodeExecutionRequest.getUserLanguage())
                .build();

        EvaluationDto evaluationDto = getQuestionDataFromQuestionServer(questionUrl+ "/api/external/execute", questionRequest);
        evaluationDto.updateWith(userCodeExecutionRequest);
        Integer executeUserCodeId = saveEvaluationDtoBeforeEvaluation(evaluationDto);
        evaluationDto.setExecuteUserCodeId(executeUserCodeId.longValue());
        return evaluationDto;
    }


    private EvaluationDto getQuestionDataFromQuestionServer(String url, EvalQuestionRequest request) {
        ResponseEntity<EvaluationDto> response = restTemplate.postForEntity(url, request, EvaluationDto.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to get valid response from " + url);
        }
    }

    private void sendToKafka(EvaluationDto evaluationDto) {
        System.out.println("Producer: " + evaluationDto);
        try{
            String jsonInString = objectMapper.writeValueAsString(evaluationDto);
            kafkaTemplate.send(TOPIC_NAME, jsonInString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves executeUsercode before evaluation
     * @param evaluationDto evaluationDto without evaluation
     * @return id of saved executeUsercode
     */
    @Transactional
    public Integer saveEvaluationDtoBeforeEvaluation(EvaluationDto evaluationDto) {
        ExecuteUsercode executeUsercode = ExecuteUsercode.builder()
                .questionId(evaluationDto.getQuestionId())
                .userLanguage(evaluationDto.getUserLanguage())
                .userCode(evaluationDto.getUserCode())
                .build();

        ExecuteUsercode response = executeUsercodeRepository.save(executeUsercode);
        return response.getId().intValue();
    }

}