package com.codementor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSolvedRatioSubmitDto {
    private Long userProblemSolvedCount;
    private Long userProblemSubmittedCount;
}
