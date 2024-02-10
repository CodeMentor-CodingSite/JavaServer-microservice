package com.codementor.dto;

import com.codementor.dto.external.QuestionDifficultyCounts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public static UserSolvedRatioTotalDto from(Long userId, List<Long> correctQuestionIdList) {
        return UserSolvedRatioTotalDto.builder()
                .userId(userId)
                .questionIdList(correctQuestionIdList)
                .build();
    }

    public UserSolvedRatioTotalDto updatedProblemCountWith(QuestionDifficultyCounts questionDifficultyCounts){
        this.easyProblemSolvedCount = questionDifficultyCounts.getEasyProblemsCount();
        this.mediumProblemSolvedCount = questionDifficultyCounts.getMediumProblemsCount();
        this.hardProblemSolvedCount = questionDifficultyCounts.getHardProblemsCount();
        return this;
    }
}