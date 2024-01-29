package com.codementor.question.dto.external;

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
}
