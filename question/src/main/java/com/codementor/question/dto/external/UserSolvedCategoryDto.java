package com.codementor.question.dto.external;

import com.codementor.question.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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

    public UserSolvedCategoryDto from(Question question){
        return UserSolvedCategoryDto.builder()
                .questionId(question.getId())
                .questionTitle(question.getTitle())
                .questionContent(question.getContent())
                .questionCategory(question.getCategory())
                .questionDifficulty(question.getDifficulty().name())
                .build();
    }
}
