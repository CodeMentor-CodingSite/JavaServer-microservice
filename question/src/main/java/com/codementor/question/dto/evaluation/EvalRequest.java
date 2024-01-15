package com.codementor.question.dto.evaluation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EvalRequest {
    private Long questionId;
    private String userLanguage;
}
