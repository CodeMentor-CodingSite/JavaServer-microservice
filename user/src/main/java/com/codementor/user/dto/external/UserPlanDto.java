package com.codementor.user.dto.external;

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

    public static UserPlanDto from(UserPlanDto userPlan) {
        return UserPlanDto.builder()
                .id(userPlan.getId())
                .planName(userPlan.getPlanName())
                .planDescription(userPlan.getPlanDescription())
                .build();
    }
}
