package com.codementor.question.dto.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EvalQuestionRequest {
    private Long questionId;
    private String userLanguage;
}
