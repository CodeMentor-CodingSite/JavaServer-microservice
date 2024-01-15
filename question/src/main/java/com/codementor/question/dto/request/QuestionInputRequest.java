package com.codementor.question.dto.request;

import com.codementor.question.enums.QuestionDifficulty;
import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionInputRequest {

    private String questionTitle;
    private String questionContent;
    private String questionCategory;
    private QuestionDifficulty questionDifficulty;
    private ArrayList<String> questionConstraintContents;
}
