package com.codementor.user.repository;

import com.codementor.user.entity.UserPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPlanRepository extends JpaRepository<UserPlan, Long>{

    Optional<UserPlan> findByUserIdAndPlanId(Long userId, Long planId);

    List<UserPlan> findAllByUserId(Long userId);
}
