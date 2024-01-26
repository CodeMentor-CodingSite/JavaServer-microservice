package com.codementor.evaluate.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 하나의 질문의 하나의 테스트케이스의 포함된 테스트케이스의 키들과 값들이 담겨있는 dto
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionTestCaseDto {
    List<QuestionTestCaseDetailsDto> questionTestCaseDetailsDtoList;
}
