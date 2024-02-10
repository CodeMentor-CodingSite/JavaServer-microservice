package com.codementor.dto;

import com.codementor.dto.external.UserSolvedQuestionIdList;
import com.codementor.entity.ExecuteUsercode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSolvedRatioSubmitDto {
    private Long userProblemSolvedCount;
    private Long userProblemSubmittedCount;

    public static UserSolvedRatioSubmitDto from(List<ExecuteUsercode> executeUsercodeList) {
        Long userSolved = executeUsercodeList.stream().filter(ExecuteUsercode::getIsCorrect).count();
        Long userSubmitted = (long) executeUsercodeList.size();
        return UserSolvedRatioSubmitDto.builder()
                .userProblemSolvedCount(userSolved)
                .userProblemSubmittedCount(userSubmitted)
                .build();
    }
}
