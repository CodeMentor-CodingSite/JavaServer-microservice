package com.codementor.dto;

import com.codementor.entity.ExecuteUsercode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecuteUsercodeDto {

    private Long id;
    private Long userId;
    private Long questionId;
    private String userCode;
    private String userLanguage;
    private Long executeTime;
    private LocalDateTime timeStamp;
    private Boolean isCorrect;
    private String gptEvaluation;
    private List<ExecuteResultDto> executeResults;

    public static ExecuteUsercodeDto from(ExecuteUsercode executeUsercode) {
        if (executeUsercode == null) {
            return null; // 또는 적절한 예외 처리
        }

        List<ExecuteResultDto> executeResultsDtos = new ArrayList<>();
        if (executeUsercode.getExecuteResults() != null) {
            executeResultsDtos = executeUsercode.getExecuteResults().stream()
                    .map(ExecuteResultDto::from)
                    .collect(Collectors.toList());
        }

        return ExecuteUsercodeDto.builder()
                .id(executeUsercode.getId())
                .userId(executeUsercode.getUserId())
                .questionId(executeUsercode.getQuestionId())
                .userCode(executeUsercode.getUserCode())
                .userLanguage(executeUsercode.getUserLanguage())
                .executeTime(executeUsercode.getExecuteTime())
                .timeStamp(executeUsercode.getTimeStamp())
                .isCorrect(executeUsercode.getIsCorrect())
                .gptEvaluation(executeUsercode.getGptEvaluation())
                .executeResults(executeResultsDtos)
                .build();
    }
}
