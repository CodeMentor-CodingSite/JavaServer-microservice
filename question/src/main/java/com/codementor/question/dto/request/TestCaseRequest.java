package com.codementor.question.dto.request;

import com.codementor.question.dto.TestCaseDetailDTO;
import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestCaseRequest {

    private Integer questionId;
    private Boolean isExample;
    private String explanation;
    private ArrayList<TestCaseDetailDTO> testCaseDetailDTOs;
}
