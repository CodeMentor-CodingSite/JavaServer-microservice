package com.codementor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "execute_result")
public class ExecuteResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "execute_result_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "execute_usercode_id")
    private ExecuteUsercode executeUsercode;

    @Column(name = "question_test_case_id")
    private Long questionTestCaseId;

    @Column(name = "testcase_result", columnDefinition = "TEXT")
    private String testcaseResult;
}
