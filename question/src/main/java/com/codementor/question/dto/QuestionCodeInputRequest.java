package com.codementor.question.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionCodeInputRequest {

    private Integer questionId;
    private String languageType;
    private String questionInitContent;
    private String answerCheckContent;
}
