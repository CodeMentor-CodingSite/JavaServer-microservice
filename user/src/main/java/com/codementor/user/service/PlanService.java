package com.codementor.user.service;

import com.codementor.user.core.util.RequestToServer;
import com.codementor.user.dto.external.UserPlanDto;
import com.codementor.user.entity.User;
import com.codementor.user.entity.UserPlan;
import com.codementor.user.repository.userplan.UserPlanRepositorySupport;
import com.codementor.user.repository.user.UserRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {

    @Value("${server.question.url}")
    private String questionoUrl;

    private final RequestToServer requestToServer;

    private final UserPlanRepositorySupport userPlanRepositorySupport;
    private final UserRepositorySupport userRepositorySupport;

    public String toggleSubscribePlan(Long userId, Long planId){
        var userPlan = userPlanRepositorySupport.findByUserIdAndPlanId(userId, planId);
        if(userPlan.isEmpty()){
            User user = userRepositorySupport.findById(userId).orElseThrow();
            userPlanRepositorySupport.save(UserPlan.subscribe(user, planId));
            return "Subscribed successfully";
        } else {
            userPlanRepositorySupport.deleteById(userPlan.get().getId());
            return "Unsubscribed successfully";
        }
    }

    public List<UserPlanDto> getPlan(Long userId){
        List<Long> userPlanIds = userPlanRepositorySupport.findAllByUserId(userId)
                .stream()
                .map(UserPlan::getPlanId)
                .collect(Collectors.toList());
        String reqUrl = questionoUrl + "/api/external/plan/get";
        return requestToServer.postDataToServer(reqUrl, userPlanIds, List.class);
    }
}
