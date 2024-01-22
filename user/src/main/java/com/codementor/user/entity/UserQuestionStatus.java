package com.codementor.user.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "user_question_status")
public class UserQuestionStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_question_status_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "question_id", columnDefinition = "BIGINT")
    private Long questionId;

    @Column(name = "user_question_status_status", columnDefinition = "TINYINT(1)")
    private Boolean status;
}
