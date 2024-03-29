package com.codementor.entity;

import com.codementor.dto.evaluation.EvalQuestionTestCaseDto;
import com.codementor.dto.evaluation.EvaluationDto;
import com.codementor.dto.request.UserCodeExecutionRequest;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @CreationTimestamp
    @Column(name = "time_stamp", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime timeStamp;

    @Column(name = "is_correct")
    private Boolean isCorrect = false;

    @Column(name = "gpt_evaluation", columnDefinition = "TEXT")
    private String gptEvaluation;

    @OneToMany(mappedBy = "executeUsercode", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExecuteResult> executeResults;

    public void updateWithIsCorrectAndExecuteTimeWith(EvaluationDto evaluationDto) {
        this.executeTime = evaluationDto.getExecuteTime();
        // 문제 정답 여부 판단
        boolean isCorrect = true;
        for (EvalQuestionTestCaseDto evalQuestionTestCaseDto : evaluationDto.getTestCaseDtoList()) {
            if (evalQuestionTestCaseDto.getTestCaseResult().equals("False\n")) {
                isCorrect = false;
                break;
            }
        }
        this.isCorrect = isCorrect;
    }

    public void updateWithGptEvaluationWith(EvaluationDto evaluationDto) {
        this.gptEvaluation = evaluationDto.getGptEvaluation();
    }

    public static ExecuteUsercode from(UserCodeExecutionRequest request, EvaluationDto dto){
        return ExecuteUsercode.builder()
                .userId(request.getUserId())
                .questionId(dto.getQuestionId())
                .userLanguage(request.getUserLanguage())
                .userCode(request.getUserCode())
                .isCorrect(false)
                .build();
    }

}
