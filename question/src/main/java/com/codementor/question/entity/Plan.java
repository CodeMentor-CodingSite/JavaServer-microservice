package com.codementor.question.entity;

import com.codementor.question.dto.request.PlanInputRequest;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "plan")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long id;

    @Column(name = "plan_name", columnDefinition = "VARCHAR(50)")
    private String planName;

    @Column(name = "plan_description", columnDefinition = "TEXT")
    private String planDescription;

    @OneToMany(mappedBy = "plan")
    private List<PlanMap> planMaps;

    public static Plan from(PlanInputRequest request) {
        return Plan.builder()
                .planName(request.getName())
                .planDescription(request.getExplanation())
                .build();
    }
}