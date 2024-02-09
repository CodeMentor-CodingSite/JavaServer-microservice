package com.codementor.dto;

import com.codementor.entity.ExecuteResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExecuteResultDto {

    private Long id;
    private Long executeUsercodeId;
    private Long questionTestCaseId;
    private String testCaseResult;

    public static ExecuteResultDto from(ExecuteResult executeResult) {
        if (executeResult == null) {
            return null;
        }
        return ExecuteResultDto.builder()
                .id(executeResult.getId())
                .executeUsercodeId(executeResult.getExecuteUsercode().getId())
                .questionTestCaseId(executeResult.getQuestionTestCaseId())
                .testCaseResult(executeResult.getTestcaseResult())
                .build();
    }
}
