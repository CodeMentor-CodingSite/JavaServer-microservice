package com.codementor.question.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EvalQuestionRequest {
    private Long questionId;
    private String userLanguage;
}
