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


    private List<EvalQuestionTestCaseDto> testCaseDtoList;
    private String answerCheckContent;


    private Long userId;
    private String userLanguage;
    private String userCode;
    private Long executeUserCodeId;

    private Long executeTime;
    private List<String> testCaseResults;
    private String gptEvaluation;

    public EvaluationDto updatedWith(UserCodeExecutionRequest userCodeExecutionRequest, Long executeUserCodeId) {
        this.userId = userCodeExecutionRequest.getUserId();
        this.userLanguage = userCodeExecutionRequest.getUserLanguage();
        this.userCode = userCodeExecutionRequest.getUserCode();
        this.executeUserCodeId = executeUserCodeId;
        return this;
    }


    @Override
    public String toString(){
        return "EvaluationDto{" +'\n' +
                "questionId=" + questionId +'\n' +
                ", questionTitle='" + questionTitle + '\'' + '\n' +
                ", questionContent='" + questionContent + '\'' +'\n' +
                ", questionCategory='" + questionCategory + '\'' +'\n' +
                ", questionConstraints=" + questionConstraints +'\n' +
                ", testCaseDtoList=" + testCaseDtoList.toString() +'\n' +
                ", answerCheckContent='" + answerCheckContent + '\'' +'\n' +
                ", userId=" + userId +'\n' +
                ", userLanguage='" + userLanguage + '\'' +'\n' +
                ", userCode='" + userCode + '\'' +'\n' +
                ", executeUserCodeId=" + executeUserCodeId +'\n' +
                ", executeTime=" + executeTime +'\n' +
                ", testCaseResults=" + testCaseResults +'\n' +
                ", gptEvaluation='" + gptEvaluation + '\'' +'\n' +
                '}';
    }
}
