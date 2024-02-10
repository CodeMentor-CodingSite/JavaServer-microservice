package com.codementor.user.service;

import com.codementor.user.entity.User;
import com.codementor.user.entity.UserPlan;
import com.codementor.user.repository.UserPlanRepository;
import com.codementor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanService {

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
}
