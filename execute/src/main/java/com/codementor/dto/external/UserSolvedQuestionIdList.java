package com.codementor.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSolvedQuestionIdList {
    private Long userId;
    private List<Long> problemIdList;

    public static UserSolvedQuestionIdList of (Long userId, List<Long> problemIdList) {
        return UserSolvedQuestionIdList.builder()
                .userId(userId)
                .problemIdList(problemIdList)
                .build();
    }
}
