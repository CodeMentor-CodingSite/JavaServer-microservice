package com.codementor.question.dto.external;

import com.codementor.question.entity.Question;
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

    public void updateWith(Question question){
        this.questionTitle = question.getTitle();
        this.difficulty = question.getDifficulty().name();
    }
}

