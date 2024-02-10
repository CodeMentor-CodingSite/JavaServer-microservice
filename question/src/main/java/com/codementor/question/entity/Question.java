package com.codementor.question.entity;

import com.codementor.question.dto.request.QuestionInputRequest;
import com.codementor.question.enums.QuestionDifficulty;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @Column(name = "question_title", columnDefinition = "VARCHAR(50)")
    private String title;

    @Column(name = "question_content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "question_category", columnDefinition = "VARCHAR(40)")
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(name = " question_difficulty", columnDefinition = "VARCHAR(40)")
    private QuestionDifficulty difficulty;

    @Column(name = "question_likes", columnDefinition = "BIGINT(20)")
    private Long likes = 0L;

    @Column(name = "question_dislikes", columnDefinition = "BIGINT(20)")
    private Long dislikes = 0L;

    @Column(name = "question_attempted", columnDefinition = "BIGINT(20)")
    private Long attempted = 0L;

    @Column(name = "question_solved", columnDefinition = "BIGINT(20)")
    private Long solved = 0L;

    @OneToMany(mappedBy = "question")
    private List<QuestionTestCase> questionTestCases;

    @OneToMany(mappedBy = "question")
    private List<QuestionConstraint> questionConstraints;

    @OneToMany(mappedBy = "question")
    private List<QuestionLanguage> questionLanguages;

    @OneToMany(mappedBy = "question")
    private List<PlanMap> planMaps;

    public static Question from(QuestionInputRequest request) {
        return Question.builder()
                .title(request.getQuestionTitle())
                .content(request.getQuestionContent())
                .category(request.getQuestionCategory())
                .difficulty(request.getQuestionDifficulty())
                .build();
    }
}
