package com.codementor.question.controller;

import com.codementor.question.core.dto.ResponseDto;
import com.codementor.question.dto.PlanDto;
import com.codementor.question.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question/plan")
public class PlanController {

    private final PlanService planService;
    @GetMapping("/all")
    public ResponseDto<List<PlanDto>> getAllPlans() {
        return ResponseDto.ok(planService.getAllPlans());
    }
}
