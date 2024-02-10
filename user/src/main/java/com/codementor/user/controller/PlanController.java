package com.codementor.user.controller;

import com.codementor.user.dto.external.UserPlanDto;
import com.codementor.user.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class PlanController {

    private final PlanService planService;

    @PutMapping("/subscribe/{planId}")
    public ResponseEntity<String> subscribePlan(@RequestHeader("userId") Long userId, @PathVariable Long planId) {
        return ResponseEntity.ok(planService.subscribePlan(userId, planId));
    }

    @GetMapping("/plan")
    public ResponseEntity<List<UserPlanDto>> getPlan(@RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(planService.getPlan(userId));
    }
}
