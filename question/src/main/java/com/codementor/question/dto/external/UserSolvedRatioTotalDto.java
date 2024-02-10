package com.codementor.question.dto.external;

import com.codementor.question.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSolvedRatioTotalDto {
    private Long userId;

    private Long easyProblemSolvedCount;
    private Long easyProblemTotalCount;
    private Long mediumProblemSolvedCount;
    private Long mediumProblemTotalCount;
    private Long hardProblemSolvedCount;
    private Long hardProblemTotalCount;

    private List<Long> questionIdList;

    public UserSolvedRatioTotalDto(Long userId, List<Long> questionIdList) {
        this.userId = userId;
        this.easyProblemSolvedCount = 0L;
        this.easyProblemTotalCount = 0L;
        this.mediumProblemSolvedCount = 0L;
        this.mediumProblemTotalCount = 0L;
        this.hardProblemSolvedCount = 0L;
        this.hardProblemTotalCount = 0L;
        this.questionIdList = questionIdList;
    }

    public UserSolvedRatioTotalDto updatedWith(Long easyProblemSolvedCount, Long mediumProblemSolvedCount, Long hardProblemSolvedCount) {
        this.easyProblemSolvedCount = easyProblemSolvedCount;
        this.mediumProblemSolvedCount = mediumProblemSolvedCount;
        this.hardProblemSolvedCount = hardProblemSolvedCount;
        return this;
    }

    public UserSolvedRatioTotalDto updatedWith(UserSolvedRatioTotalDto req, List<Question> questionsList) {
        var questionSolvedList = req.getQuestionIdList();
        Long easyProblemTotalCount = 0L;
        Long mediumProblemTotalCount = 0L;
        Long hardProblemTotalCount = 0L;

        for (Question question : questionsList) {
            if (questionSolvedList.contains(question.getId())) {
                switch (question.getDifficulty()) {
                    case EASY:
                        easyProblemTotalCount++;
                        break;
                    case MEDIUM:
                        mediumProblemTotalCount++;
                        break;
                    case HARD:
                        hardProblemTotalCount++;
                        break;
                }
            }
        }

        this.easyProblemTotalCount = easyProblemTotalCount;
        this.mediumProblemTotalCount = mediumProblemTotalCount;
        this.hardProblemTotalCount = hardProblemTotalCount;
        return this;
    }
}