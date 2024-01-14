package com.codementor.execute.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class ExecuteResultDTO {
    private Long checkOutId;
    private String userCode;
    private Float executeTime;
    private String language;
    private String gptEvaluation;
    private String gptCode;
}
