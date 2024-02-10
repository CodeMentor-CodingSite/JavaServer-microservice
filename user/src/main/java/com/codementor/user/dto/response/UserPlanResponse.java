package com.codementor.user.dto.response;

import com.codementor.user.entity.UserPlan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserPlanResponse {

    private Long id;
    private Long user;
    private Long planId;

    public static UserPlanResponse from(UserPlan userPlan) {
        return UserPlanResponse.builder()
                .id(userPlan.getId())
                .user(userPlan.getUser().getId())
                .planId(userPlan.getPlanId())
                .build();
    }
}
