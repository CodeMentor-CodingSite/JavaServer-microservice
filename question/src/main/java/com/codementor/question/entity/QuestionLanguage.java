package com.codementor.question.entity;

import com.codementor.question.dto.request.QuestionCodeInputRequest;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "question_language")
public class QuestionLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_language_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "language_id")
    private Language language;

    @Column(name = "question_init_content", columnDefinition = "TEXT")
    private String initContent;

    @Column(name = "answer_check_content", columnDefinition = "TEXT")
    private String checkContent;

    public static QuestionLanguage from(Question question, Language language, QuestionCodeInputRequest questionCodeInputRequest) {
        return QuestionLanguage.builder()
                .question(question)
                .language(language)
                .initContent(questionCodeInputRequest.getQuestionInitContent())
                .checkContent(questionCodeInputRequest.getAnswerCheckContent())
                .build();
    }
}
