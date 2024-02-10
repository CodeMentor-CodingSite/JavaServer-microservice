package com.codementor.dto.evaluation;

import com.codementor.dto.request.UserCodeExecutionRequest;
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

    public static EvalQuestionRequest from(UserCodeExecutionRequest userCodeExecutionRequest) {
        return EvalQuestionRequest.builder()
                .questionId(userCodeExecutionRequest.getQuestionId())
                .userLanguage(userCodeExecutionRequest.getUserLanguage())
                .build();
    }
}
