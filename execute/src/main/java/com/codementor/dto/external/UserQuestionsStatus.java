package com.codementor.dto.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserQuestionsStatus {
    private List<Long> attmptedQuestions;
    private List<Long> solvedQuestions;
}
