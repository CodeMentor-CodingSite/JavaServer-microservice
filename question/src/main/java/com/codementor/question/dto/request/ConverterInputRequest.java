package com.codementor.question.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConverterInputRequest {

    private String languageType;
    private String codeExecConverterContent;
    private String resultType;
    private String methodName;
}
