package com.codementor.dto.external;

import com.codementor.entity.ExecuteUsercode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserQuestionsStatus {
    private List<Long> attmptedQuestions;
    private List<Long> solvedQuestions;

    public static UserQuestionsStatus from(List<ExecuteUsercode> executeUsercodeList) {
        Set<Long> attemptedQuestionIds = new HashSet<>();
        Set<Long> solvedQuestionIds = new HashSet<>();
        for (ExecuteUsercode executeUsercode : executeUsercodeList) {
            if (executeUsercode.getIsCorrect()) {
                solvedQuestionIds.add(executeUsercode.getQuestionId());
            } else {
                attemptedQuestionIds.add(executeUsercode.getQuestionId());
            }
        }
        return UserQuestionsStatus.builder()
                .attmptedQuestions(new ArrayList<>(attemptedQuestionIds))
                .solvedQuestions(new ArrayList<>(solvedQuestionIds))
                .build();
    }
}
