package com.codementor.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSolvedCategoryDto {
    private Long questionId;
    private String questionTitle;
    private String questionContent;
    private String questionCategory;
    private String questionDifficulty;
}
