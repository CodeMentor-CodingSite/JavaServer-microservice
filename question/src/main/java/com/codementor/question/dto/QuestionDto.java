package com.codementor.question.dto;

import com.codementor.question.entity.Question;
import com.codementor.question.enums.UserSolvedStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionDto {
    private Long questionId;
    private String questionTitle;
    private String questionContent;
    private String questionCategory;
    private String questionDifficulty;
    private Long likes;
    private Long dislikes;
    private Long usersAttempts;
    private Long usersSolved;
    private String userSolved;

    public static QuestionDto from(Question question, UserSolvedStatus userSolved){
        return QuestionDto.builder()
                .questionId(question.getId())
                .questionTitle(question.getTitle())
                .questionContent(question.getContent())
                .questionCategory(question.getCategory())
                .questionDifficulty(question.getDifficulty().name())
                .likes(question.getLikes())
                .dislikes(question.getDislikes())
                .usersAttempts(question.getAttempted())
                .usersSolved(question.getSolved())
                .userSolved(userSolved.name())
                .build();
    }
}
