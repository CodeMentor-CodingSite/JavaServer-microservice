package com.codementor.service;

import com.codementor.core.exception.CodeMentorException;
import com.codementor.core.exception.ErrorEnum;
import com.codementor.dto.evaluation.EvalQuestionTestCaseDto;
import com.codementor.dto.evaluation.EvaluationDto;
import com.codementor.entity.ExecuteResult;
import com.codementor.entity.ExecuteUsercode;
import com.codementor.repository.ExecuteResultRepository;
import com.codementor.repository.ExecuteUsercodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserCodeExecutionResponseConsumer {

    private final SseConnectionService sseConnectionService;

    private final ExecuteUsercodeRepository executeUsercodeRepository;
    private final ExecuteResultRepository executeResultRepository;

    /**
     * kafka로부터 유저코드 실행 결과가 포함된 EvaluationDTO 또는 gpt 평과값까지 포함된 EvaluationDTO를 받아서 처리한다.
     * @param evaluationDto : kafka 메시지로부터 받은 EvaluationDto
     * 1. 코드 실행 결과만 있는 dto가 오면, 기존 결과가 저장되어있지 않은 execute_usercode에 대한 코드 실행결과를 추가하여 저장하고, execute_result에 각 테스트케이스에 대한 결과를 저장.
     * 2. sse로 결과 전송
     * 3. 기존 결과가 저장되어있지 않은 execute_usercode 엔터티에 gpt 평가값을 추가하여 저장.
     */
    @Transactional
    public void receivedEvaluationResult(EvaluationDto evaluationDto) {
        ExecuteUsercode executeUsercode = executeUsercodeRepository.findById(evaluationDto.getExecuteUserCodeId())
                .orElseThrow(() -> new CodeMentorException(ErrorEnum.RECORD_NOT_FOUND));
        if (evaluationDto.getGptEvaluation() == null){
            executeUsercode.updateWithIsCorrectAndExecuteTimeWith(evaluationDto); // 1.
            saveExecuteResultForEachTestCases(evaluationDto, executeUsercode);
            sseConnectionService.sendToUser(evaluationDto.getUserId().toString(), executeUsercode.getId()); // 2.
        } else {
            executeUsercode.updateWithGptEvaluationWith(evaluationDto); // 3.
        }
        executeUsercodeRepository.save(executeUsercode);
    }

    /**
     * execute_result 테이블에 각 테스트케이스에 대한 결과를 저장한다.
     * @param evaluationDto kafka 메시지로부터 받은 EvaluationDto
     * @param executeUsercode 기존 결과가 저장되어있지 않은 execute_usercode 엔터티
     */
    private void saveExecuteResultForEachTestCases(EvaluationDto evaluationDto, ExecuteUsercode executeUsercode) {
        for (EvalQuestionTestCaseDto evalQuestionTestCaseDto : evaluationDto.getTestCaseDtoList()) {
            ExecuteResult executeResult = ExecuteResult.builder()
                    .executeUsercode(executeUsercode)
                    .questionTestCaseId(evalQuestionTestCaseDto.getTestCaseId())
                    .testcaseResult(evalQuestionTestCaseDto.getTestCaseResult())
                    .build();
            executeResultRepository.save(executeResult);
        }
    }

}
