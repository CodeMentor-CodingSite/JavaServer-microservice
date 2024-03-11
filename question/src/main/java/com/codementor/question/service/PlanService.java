package com.codementor.question.service;

import com.codementor.question.dto.PlanDto;
import com.codementor.question.entity.Plan;
import com.codementor.question.repository.Plan.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;

    public List<PlanDto> getAllPlans() {
        List<Plan> plans = planRepository.findAll();
        List<PlanDto> planDtos = new ArrayList<>();
        for (Plan plan : plans) {
            List<Long> questionIdList = plan.getPlanMaps().stream()
                    .map(planMap -> planMap.getQuestion().getId())
                    .collect(Collectors.toList());
            planDtos.add(PlanDto.from(plan, questionIdList));
        }
        return planDtos;
    }
}
