package com.codementor.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionTestCaseDto {
    private Long id;
    private Boolean isExample;
    private String explanation;
    private List<QuestionTestCaseDetailDto> questionTestCaseDetailDtoList;
}
