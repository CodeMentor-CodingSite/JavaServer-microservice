package com.codementor.question.dto.external;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvalTestCaseDto {

    private Long testCaseId;
    private Boolean isExample;
    private String explanation;
    private List<EvalTestCaseDetailAndConverterDto> evalTestCaseDetailAndConverterDtos;
    private String testCaseResult;
}
