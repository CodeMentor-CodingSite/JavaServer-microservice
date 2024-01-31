package com.codementor.dto.evaluation;

import com.codementor.dto.request.UserCodeExecutionRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EvaluationDto {

    private Long questionId;
    private String questionTitle;
    private String questionContent;
    private String questionCategory;
    private List<String> questionConstraints;


    private List<EvalTestCaseDto> testCaseDtoList;
    private String answerCheckContent;


    private Long userId;
    private String userLanguage;
    private String userCode;
    private Long executeUserCodeId;

    private Long executeTime;
    private List<String> testCaseResults;
    private String gptEvaluation;

    public void updateWith(UserCodeExecutionRequest userCodeExecutionRequest, Long executeUserCodeId) {
        this.userId = userCodeExecutionRequest.getUserId();
        this.userLanguage = userCodeExecutionRequest.getUserLanguage();
        this.userCode = userCodeExecutionRequest.getUserCode();
        this.executeUserCodeId = executeUserCodeId;
    }
}
