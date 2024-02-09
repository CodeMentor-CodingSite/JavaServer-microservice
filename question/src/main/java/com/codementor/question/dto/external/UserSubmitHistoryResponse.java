package com.codementor.question.dto.external;

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
}
