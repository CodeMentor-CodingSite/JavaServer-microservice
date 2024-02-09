package com.codementor.dto.response;

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
public class UserSolvedQuestionIdAndTitleAndTimeResponse {

    private Long usercodeId;
    private Long questionId;
    private String questionTitle;
    private LocalDateTime timeStamp;
    private String difficulty;

    public static UserSolvedQuestionIdAndTitleAndTimeResponse of(ExecuteUsercode executeUserCode, String difficulty){
        return UserSolvedQuestionIdAndTitleAndTimeResponse.builder()
                .usercodeId(executeUserCode.getId())
                .questionId(executeUserCode.getQuestionId())
                .difficulty(difficulty)
                .timeStamp(executeUserCode.getTimeStamp())
                .build();
    }
}

