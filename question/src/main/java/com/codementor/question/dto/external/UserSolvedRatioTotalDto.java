package com.codementor.question.dto.external;

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

    public UserSolvedRatioTotalDto addProblemSolvedCounts(Long easyProblemSolvedCount, Long mediumProblemSolvedCount, Long hardProblemSolvedCount) {
        this.easyProblemSolvedCount = easyProblemSolvedCount;
        this.mediumProblemSolvedCount = mediumProblemSolvedCount;
        this.hardProblemSolvedCount = hardProblemSolvedCount;
        return this;
    }
}