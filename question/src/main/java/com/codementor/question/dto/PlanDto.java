package com.codementor.question.dto;


import com.codementor.question.entity.Plan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanDto {
    private Long planId;
    private String planName;
    private String planDescription;
    private List<Long> questionIdList;

    public static PlanDto from(Plan plan) {
        List<Long> questionIdList = plan.getPlanMaps().stream()
                .map(planMap -> planMap.getQuestion().getId())
                .collect(Collectors.toList());

        return PlanDto.builder()
                .planId(plan.getId())
                .planName(plan.getPlanName())
                .planDescription(plan.getPlanExplanation())
                .questionIdList(questionIdList)
                .build();
    }
}
