package com.codementor.user.service;

import com.codementor.user.core.util.RequestToServer;
import com.codementor.user.dto.external.UserPlanDto;
import com.codementor.user.entity.User;
import com.codementor.user.entity.UserPlan;
import com.codementor.user.repository.UserPlanRepository;
import com.codementor.user.repository.UserRepository;
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

    private final UserPlanRepository userPlanRepository;
    private final UserRepository userRepository;

    public String subscribePlan(Long userId, Long planId){
        var userPlan = userPlanRepository.findByUserIdAndPlanId(userId, planId);
        if(userPlan.isPresent()){
            userPlanRepository.deleteById(userPlan.get().getId());
            return "Unsubscribed successfully";
        } else {
            User user = userRepository.findById(userId).orElseThrow();
            userPlanRepository.save(UserPlan.subscribe(user, planId));
            return "Subscribed successfully";
        }
    }

    public List<UserPlanDto> getPlan(Long userId){
        List<Long> userPlanIds = userPlanRepository.findAllByUserId(userId)
                .stream()
                .map(UserPlan::getPlanId)
                .collect(Collectors.toList());
        String reqUrl = questionoUrl + "/api/external/plan/get";
        return requestToServer.postDataToServer(reqUrl, userPlanIds, List.class);
    }
}
