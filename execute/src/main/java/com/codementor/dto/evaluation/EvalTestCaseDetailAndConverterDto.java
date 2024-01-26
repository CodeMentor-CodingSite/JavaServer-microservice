package com.codementor.dto.evaluation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EvalTestCaseDetailAndConverterDto {
    // CodeExecConverter 필드
    private String codeExecConverterContent;
    private String returnType;
    private String methodName;

    // QuestionTestCaseDetail 필드
    private String testCaseKey;
    private String testCaseValue;
}
