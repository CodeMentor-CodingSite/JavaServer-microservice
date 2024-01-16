package com.codementor.execute.dto.evaluation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EvalQuestionTestCaseDetailAndConverterDto {
    // CodeExecConverter 필드
    private String codeExecConverterContent;
    private String returnType;
    private String methodName;

    // QuestionTestCaseDetail 필드
    private String testCaseKey;
    private String testCaseValue;
}
