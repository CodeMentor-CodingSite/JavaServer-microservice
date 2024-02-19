package com.codementor.question.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "question_constraint")
public class QuestionConstraint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_constraint_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "question_constraint_content", columnDefinition = "VARCHAR(300)")
    private String content;

    public static QuestionConstraint from(Question savedQuestion, String content) {
        return QuestionConstraint.builder()
                .question(savedQuestion)
                .content(content)
                .build();
    }
}
