package com.codementor.question.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class QuestionDifficultyCounts {

    private Long easyProblemsCount;
    private Long mediumProblemsCount;
    private Long hardProblemsCount;

}
