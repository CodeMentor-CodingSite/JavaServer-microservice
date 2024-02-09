package com.codementor.question.controller;

import com.codementor.question.dto.request.PlanInputRequest;
import com.codementor.question.service.PlanInputService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question/plan/input/")
public class PlanInputController {

    private final PlanInputService planInputService;
    @PostMapping("/plan")
    public ResponseEntity planInput(@RequestBody PlanInputRequest request) {
        return ResponseEntity.ok(planInputService.planInput(request));
    }
}
