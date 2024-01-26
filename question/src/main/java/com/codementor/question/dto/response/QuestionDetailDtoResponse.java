package com.codementor.question.dto.response;

import com.codementor.question.dto.QuestionTestCaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionDetailDtoResponse {
    private Long questionId;
    private String questionTitle;
    private String questionContent;
    private String questionCategory;
    private String questionDifficulty;
    private Long likes;
    private Long dislikes;
    private Long usersAttempts;
    private Long usersSolved;
    private List<QuestionTestCaseDto> questionTestCaseDtoList;
    private List<String> constraints;
    private List<String> languages;

}
