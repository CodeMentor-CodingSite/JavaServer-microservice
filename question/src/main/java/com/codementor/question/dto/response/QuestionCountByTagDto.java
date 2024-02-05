package com.codementor.question.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class QuestionCountByTagDto {
    private Long userId;
    private HashMap<String, Long> questionCountByTag;
}
