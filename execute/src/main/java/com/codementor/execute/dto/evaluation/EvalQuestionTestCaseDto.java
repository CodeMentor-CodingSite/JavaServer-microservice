package com.codementor.execute.dto.evaluation;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvalQuestionTestCaseDto {

    private Long testCaseId;
    private Boolean isExample;
    private String explanation;
    private List<EvalQuestionTestCaseDetailAndConverterDto> evalQuestionTestCaseDetailAndConverterDtos;
}
