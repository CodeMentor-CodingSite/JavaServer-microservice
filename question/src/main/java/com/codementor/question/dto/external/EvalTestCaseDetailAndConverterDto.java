package com.codementor.question.dto.external;

import com.codementor.question.entity.CodeExecConverter;
import com.codementor.question.entity.QuestionTestCaseDetail;
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

    public void updateWith(CodeExecConverter converter){
        this.codeExecConverterContent= converter.getContent();
        this.returnType = converter.getReturnType();
        this.methodName = converter.getMethodName();
    }

    public static EvalTestCaseDetailAndConverterDto from(QuestionTestCaseDetail detail){
        return EvalTestCaseDetailAndConverterDto.builder()
                .testCaseKey(detail.getKey())
                .testCaseValue(detail.getValue())
                .build();
    }
}
