package com.codementor.question.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionCodeInputRequest {

    private Long questionId;
    private String languageType;
    private String questionInitContent;
    private String answerCheckContent;
}
