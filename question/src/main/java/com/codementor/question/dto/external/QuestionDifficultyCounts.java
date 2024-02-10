package com.codementor.question.dto.external;

import com.codementor.question.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class QuestionDifficultyCounts {

    private Long easyProblemsCount;
    private Long mediumProblemsCount;
    private Long hardProblemsCount;

    public static QuestionDifficultyCounts fromQuestionList(Iterable<Question> questionsList) {
        Long easy = 0L;
        Long medium = 0L;
        Long hard = 0L;

        for (Question question : questionsList) {
            switch (question.getDifficulty()) {
                case EASY:
                    easy++;
                    break;
                case MEDIUM:
                    medium++;
                    break;
                case HARD:
                    hard++;
                    break;
            }
        }
        return QuestionDifficultyCounts.builder()
                .easyProblemsCount(easy)
                .mediumProblemsCount(medium)
                .hardProblemsCount(hard)
                .build();
    }
}
