package com.codementor.question.dto.external;

import com.codementor.question.entity.Plan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserPlanDto {

    private Long id;
    private String planName;
    private String planDescription;

    public static UserPlanDto from(Plan plan) {
        return UserPlanDto.builder()
                .id(plan.getId())
                .planName(plan.getPlanName())
                .planDescription(plan.getPlanDescription())
                .build();
    }
}
