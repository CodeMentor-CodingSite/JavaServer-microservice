package com.codementor.evaluate.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 코드 실행에 필요한 컨버터의 메서드 이름, 메서드 컨텐트, 리턴 타입을 담은 dto
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CodeExecutionConverterDto {
    private String methodName;
    private String codeExecutionConverterContent;
    private String returnType;
}
