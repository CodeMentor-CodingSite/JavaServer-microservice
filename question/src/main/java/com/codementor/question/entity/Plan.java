package com.codementor.question.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "package")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id")
    private Long id;

    @Column(name = "package_name", columnDefinition = "VARCHAR(50)")
    private String planName;

    @Column(name = "package_explanation", columnDefinition = "TEXT")
    private String planExplanation;
}
