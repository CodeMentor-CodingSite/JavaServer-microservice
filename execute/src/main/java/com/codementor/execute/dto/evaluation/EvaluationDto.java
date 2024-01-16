package com.codementor.execute.dto.evaluation;

import com.codementor.execute.dto.request.UserCodeExecutionRequest;
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


    private List<EvalQuestionTestCaseDto> testCaseDtoList;
    private String answerCheckContent;


    private Long userId;
    private String userLanguage;
    private String userCode;


    private List<String> testCaseResults;
    private String gptEvaluation;

    public EvaluationDto updateWith(UserCodeExecutionRequest userCodeExecutionRequest) {
        this.userId = userCodeExecutionRequest.getUserId();
        this.userLanguage = userCodeExecutionRequest.getUserLanguage();
        this.userCode = userCodeExecutionRequest.getUserCode();
        return this;
    }
}