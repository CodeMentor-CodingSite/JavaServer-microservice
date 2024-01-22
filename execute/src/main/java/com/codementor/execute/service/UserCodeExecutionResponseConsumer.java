package com.codementor.execute.service;

import com.codementor.execute.dto.evaluation.EvalTestCaseDto;
import com.codementor.execute.dto.evaluation.EvaluationDto;
import com.codementor.execute.entity.ExecuteResult;
import com.codementor.execute.entity.ExecuteUsercode;
import com.codementor.execute.kafkaTest.MessageDTO;
import com.codementor.execute.repository.ExecuteResultRepository;
import com.codementor.execute.repository.ExecuteUsercodeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserCodeExecutionResponseConsumer {

    private static final String TOPIC_NAME = "usercode.response.topic.v1";
    private static final String GROUP_ID = "usercode.response.group.v1";

    private final ObjectMapper objectMapper;

    private final SseConnectionService sseConnectionService;

    private final ExecuteUsercodeRepository executeUsercodeRepository;
    private final ExecuteResultRepository executeResultRepository;

    /**
     * kafka로부터 유저코드 실행 결과가 포함된 EvaluationDTO 또는 gpt 평과값까지 포함된
     */
    @KafkaListener(topics = TOPIC_NAME, groupId = GROUP_ID)
    public void kafkaListener(String jsonMessage) {
        try {
            EvaluationDto evaluationDto = objectMapper.readValue(jsonMessage, EvaluationDto.class);
            System.out.println("Consumer: " + evaluationDto);

            // 유저코드 실행결과만 저장된 메세지인지 확인.
            String gptEvaluation = evaluationDto.getGptEvaluation();

            if (gptEvaluation == null){
                Long executeUsercodeId = saveEvaluationWithCodeExecutionResult(evaluationDto);
                String userId = evaluationDto.getUserId().toString();
                // SseConnection으로 유저에게 메세지 전송
                sseConnectionService.sendToUser(userId, executeUsercodeId);
            } else {
                Long executeUsercodeId = saveEvaluationWithGptResult(evaluationDto);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 코드 실행 결과만 있는 dto가 오면,
     * 기존 결과가 저장되어있지 않은 execute_usercode에 대한 코드 실행결과를 추가하여 저장하고, execute_result에 각 테스트케이스에 대한 결과를 저장.
     * @param evaluationDto : 유저코드 실행 결과가 포함된 EvaluationDto
     * @return execute_usercode_id
     */
    @Transactional
    public Long saveEvaluationWithCodeExecutionResult(EvaluationDto evaluationDto) {
        Long executeUserCodeId = evaluationDto.getExecuteUserCodeId();
        boolean isCorrect = true;

        ExecuteUsercode executeUsercode = executeUsercodeRepository.findById(executeUserCodeId).get();
        executeUsercode.setExecuteTime(evaluationDto.getExecuteTime());

        // 문제 정답 여부 판단
        for (EvalTestCaseDto evalTestCaseDto : evaluationDto.getTestCaseDtoList()) {
            if (evalTestCaseDto.getTestCaseResult().equals("False")) {
                isCorrect = false;
                break;
            }
        }
        executeUsercode.setExecuteTime(evaluationDto.getExecuteTime());
        executeUsercode.setIsCorrect(isCorrect);
        // ExecuteUsercode 저장
        executeUsercodeRepository.save(executeUsercode);

        // Todo : 이 부분 분리를 할지 고민해봐야됨. 하나의 메서드에서 두개의 repository에 저장하는건 좋지 않은거 같음.
        // 각 테스트케이스에 대한 결과를 저장.
        for (int i = 0; i < evaluationDto.getTestCaseResults().size(); i++) {
            String codeExecutionResult = evaluationDto.getTestCaseResults().get(i);

            ExecuteResult executeResult = ExecuteResult.builder()
                    .executeUsercode(executeUsercode)
                    .questionTestCaseId(evaluationDto.getTestCaseDtoList().get(i).getTestCaseId())
                    .testcaseResult(codeExecutionResult)
                    .build();

            evaluationDto.getTestCaseDtoList().get(i).setTestCaseResult(codeExecutionResult);
            executeResultRepository.save(executeResult);
        }
        return executeUserCodeId;
    }

    /**
     * gpt 평가값이 포함된 EvaluationDto가 오면,
     * 기존 결과가 저장되어있지 않은 execute_usercode에 대한 gpt 평가값을 추가하여 저장.
     * @param evaluationDto : gpt 평가값이 포함된 EvaluationDto
     * @return  execute_usercode_id
     */
    @Transactional
    public Long saveEvaluationWithGptResult(EvaluationDto evaluationDto) {
        Long executeUserCodeId = evaluationDto.getExecuteUserCodeId();

        ExecuteUsercode executeUsercode = executeUsercodeRepository.findById(executeUserCodeId).get();
        executeUsercode.setGptEvaluation(evaluationDto.getGptEvaluation());
        executeUsercodeRepository.save(executeUsercode);
        return executeUserCodeId;
    }
}
