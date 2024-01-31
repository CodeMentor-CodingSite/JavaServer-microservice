package com.codementor.service;

import com.codementor.core.exception.CodeMentorException;
import com.codementor.core.exception.ErrorEnum;
import com.codementor.core.util.RequestToServer;
import com.codementor.core.util.SendToKafka;
import com.codementor.dto.evaluation.EvalQuestionRequest;
import com.codementor.dto.evaluation.EvaluationDto;
import com.codementor.dto.request.UserCodeExecutionRequest;
import com.codementor.entity.ExecuteUsercode;
import com.codementor.repository.ExecuteUsercodeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserCodeExecutionRequestProducer {

    @Value("${server.question.url}")
    private String questionUrl;

    private final RequestToServer requestToServer;
    private final SendToKafka sendToKafka;

    private final ExecuteUsercodeRepository executeUsercodeRepository;

    private static final String TOPIC_NAME = "usercode.request.topic.v1";
    private static final String GROUP_ID = "usercode.request.group.v1";


    /**
     * 유저의 코드 실행 요청을 평가 전에 저장한다.
     * EvaluationDto를 카프카로 보낸다.
     * @param userCodeExecutionRequest 유저의 코드 실행 및 문제와 관련된 데이터 객체
     * 1. EvaluationDto를 평가전에 저장한다.
     * 2. EvaluationDto를 평가전에 저장한다.
     * 3. EvaluationDto를 유저의 코드 실행 요청 데이터와 저장된 executeUsercodeId로 업데이트한다.
     * 4. EvaluationDto를 카프카로 보낸다.
     */
    @Transactional
    public void sendUserCodeExecutionRequestToKafka(UserCodeExecutionRequest userCodeExecutionRequest) {
        EvalQuestionRequest evalQuestionRequest = EvalQuestionRequest.builder() // 1.
                .questionId(userCodeExecutionRequest.getQuestionId())
                .userLanguage(userCodeExecutionRequest.getUserLanguage())
                .build();

//        System.out.println(evalQuestionRequest.toString());
        String questionDataFromQuestionServerUrl = questionUrl + "/api/external/execute";
        EvaluationDto evaluationDto = requestToServer.postDataToServer(
                questionDataFromQuestionServerUrl,
                evalQuestionRequest,
                EvaluationDto.class);

        ExecuteUsercode executeUsercode = ExecuteUsercode.builder() // 2.
                .questionId(evaluationDto.getQuestionId())
                .userLanguage(evaluationDto.getUserLanguage())
                .userCode(evaluationDto.getUserCode())
                .build();
        Long executeUsercodeId = executeUsercodeRepository.save(executeUsercode).getId();

        evaluationDto.updateWith(userCodeExecutionRequest, executeUsercodeId); // 3.

        sendToKafka.sendData(TOPIC_NAME, evaluationDto); // 4.
    }
}