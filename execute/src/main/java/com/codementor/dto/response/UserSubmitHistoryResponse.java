package com.codementor.dto.response;

import com.codementor.entity.ExecuteUsercode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserSubmitHistoryResponse {

    private Long usercodeId;
    private Long questionId;
    private String questionName;
    private Boolean isCorrect;
    private Long executeTime;
    private String userLanguage;
    private String gptEvaluation;
    private LocalDateTime timeStamp;

    public static UserSubmitHistoryResponse of(ExecuteUsercode executeUsercode) {
        return UserSubmitHistoryResponse.builder()
                .usercodeId(executeUsercode.getId())
                .questionId(executeUsercode.getQuestionId())
                .isCorrect(executeUsercode.getIsCorrect())
                .executeTime(executeUsercode.getExecuteTime())
                .userLanguage(executeUsercode.getUserLanguage())
                .gptEvaluation(executeUsercode.getGptEvaluation())
                .timeStamp(executeUsercode.getTimeStamp())
                .build();
    }
}
