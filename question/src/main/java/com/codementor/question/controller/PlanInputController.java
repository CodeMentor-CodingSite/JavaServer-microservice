package com.codementor.question.controller;

import com.codementor.question.dto.request.PlanInputRequest;
import com.codementor.question.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plan/input/")
public class PlanInputController {

    private final PlanService planService;
    @PostMapping("/plan")
    public ResponseEntity planInput(@RequestBody PlanInputRequest request) {
        return ResponseEntity.ok(planService.planInput(request));
    }
}
