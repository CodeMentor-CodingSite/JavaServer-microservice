package com.codementor.question.dto.external;

import com.codementor.question.entity.QuestionTestCase;
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

    public static EvalTestCaseDto from(QuestionTestCase testcase, List<EvalTestCaseDetailAndConverterDto> dto){
        return EvalTestCaseDto.builder()
                .testCaseId(testcase.getId())
                .isExample(testcase.getIsExample())
                .explanation(testcase.getExplanation())
                .evalTestCaseDetailAndConverterDtos(dto)
                .testCaseResult("processing")
                .build();
    }
}
