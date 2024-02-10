package com.codementor.question.dto.external;

import com.codementor.question.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

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

    public void updateWith(Question question){
        this.questionTitle = question.getTitle();
        this.difficulty = question.getDifficulty().name();
    }

    public static UserSolvedQuestionIdAndTitleAndTimeResponse of(
            UserSolvedQuestionIdAndTitleAndTimeResponse req,
            Optional<Question> question){
        return UserSolvedQuestionIdAndTitleAndTimeResponse.builder()
                .usercodeId(req.getUsercodeId())
                .questionId(question.get().getId())
                .questionTitle(question.get().getTitle())
                .difficulty(req.getDifficulty())
                .timeStamp(req.getTimeStamp())
                .build();
    }
}

