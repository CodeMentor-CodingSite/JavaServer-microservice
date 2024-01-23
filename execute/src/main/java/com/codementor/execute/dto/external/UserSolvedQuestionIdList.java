package com.codementor.execute.dto.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSolvedQuestionIdList {
    private Long userId;
    private List<Long> problemIdList;
}
