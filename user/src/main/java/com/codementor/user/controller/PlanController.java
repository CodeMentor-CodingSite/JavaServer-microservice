package com.codementor.user.controller;

import com.codementor.user.core.dto.ResponseDto;
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
    public ResponseDto<String> subscribePlan(@RequestHeader("userId") Long userId, @PathVariable Long planId) {
        return ResponseDto.ok(planService.subscribePlan(userId, planId));
    }

    @GetMapping("/plan")
    public ResponseDto<List<UserPlanDto>> getPlan(@RequestHeader("userId") Long userId) {
        return ResponseDto.ok(planService.getPlan(userId));
    }
}
