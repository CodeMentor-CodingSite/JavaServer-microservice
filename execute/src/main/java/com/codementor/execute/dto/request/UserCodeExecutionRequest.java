package com.codementor.execute.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 유저가 입력한 코드를 실행하기 위한 request dto
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCodeExecutionRequest {

    private Long userId;
    private String userLanguage;
    private String userCode;
    private Long questionId;
}
