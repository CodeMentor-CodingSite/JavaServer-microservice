package com.codementor.execute.entity;

import com.codementor.execute.dto.evaluation.EvalTestCaseDetailAndConverterDto;
import com.codementor.execute.dto.evaluation.EvalTestCaseDto;
import com.codementor.execute.dto.evaluation.EvaluationDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "execute_usercode")
public class ExecuteUsercode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "execute_usercode_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "user_code", columnDefinition = "TEXT")
    private String userCode;

    @Column(name = "user_language", columnDefinition = "VARCHAR(20)")
    private String userLanguage;

    @Column(name = "execute_time")
    private Long executeTime;

    @Column(name = "time_stamp", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private String timeStamp;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "gpt_evaluation", columnDefinition = "TEXT")
    private String gptEvaluation;

    @OneToMany(mappedBy = "executeUsercode")
    private List<ExecuteResult> executeResults;

}
