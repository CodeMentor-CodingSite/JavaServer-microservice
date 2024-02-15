package com.codementor.dto;

import com.codementor.entity.ExecuteUsercode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecuteAllUsercodeDto {

    private Long id;
    private Long userId;
    private String userCode;
    private String userLanguage;
    private Long executeTime;
    private LocalDateTime timeStamp;
    private Boolean isCorrect;
    private String gptEvaluation;

    public static ExecuteAllUsercodeDto from(ExecuteUsercode executeUsercode) {
        return ExecuteAllUsercodeDto.builder()
                .id(executeUsercode.getId())
                .userId(executeUsercode.getUserId())
                .userCode(executeUsercode.getUserCode())
                .userLanguage(executeUsercode.getUserLanguage())
                .executeTime(executeUsercode.getExecuteTime())
                .timeStamp(executeUsercode.getTimeStamp())
                .isCorrect(executeUsercode.getIsCorrect())
                .gptEvaluation(executeUsercode.getGptEvaluation())
                .build();
    }
}
