package com.codementor.service;

import com.codementor.core.util.RequestToServer;
import com.codementor.core.util.SendToKafka;
import com.codementor.dto.evaluation.EvalQuestionRequest;
import com.codementor.dto.evaluation.EvaluationDto;
import com.codementor.dto.request.UserCodeExecutionRequest;
import com.codementor.entity.ExecuteUsercode;
import com.codementor.repository.ExecuteUsercode.ExecuteUsercodeRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCodeExecutionRequestProducer {

    @Value("${server.question.url}")
    private String questionUrl;

    private final RequestToServer requestToServer;
    private final SendToKafka sendToKafka;

    private final ExecuteUsercodeRepositorySupport executeUsercodeRepositorySupport;

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
        var questionDataFromQuestionServerUrl = questionUrl + "/api/external/execute";
        var evaluationDto = requestToServer.postDataToServer(
                questionDataFromQuestionServerUrl,
                EvalQuestionRequest.from(userCodeExecutionRequest), // 1.
                EvaluationDto.class);
        var executeUsercodeId = executeUsercodeRepositorySupport.save(ExecuteUsercode.from(userCodeExecutionRequest, evaluationDto)).getId(); // 2
        System.out.println("Sending to Kafka");
        sendToKafka.sendData(TOPIC_NAME, evaluationDto.updatedWith(userCodeExecutionRequest, executeUsercodeId)); // 3, 4.
    }
}