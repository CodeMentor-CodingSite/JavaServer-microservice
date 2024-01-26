package com.codementor.evaluate.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 하나의 질문의 하나의 테스트케이스의 포함된 테스트케이스의 키와 값
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionTestCaseDetailsDto {
    private String key;
    private String value;
    private Integer applyConverter;
}
