package com.codementor.evaluate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EvaluationQuestionDto {

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
}
