package com.codementor.user.controller;

import com.codementor.user.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class PlanController {

    private final PlanService planService;

    @PutMapping("/subscribe")
    public ResponseEntity<String> subscribePlan(@RequestHeader("userId") Long userId, @RequestBody Long planId) {
        return ResponseEntity.ok(planService.subscribePlan(userId, planId));
    }
}
