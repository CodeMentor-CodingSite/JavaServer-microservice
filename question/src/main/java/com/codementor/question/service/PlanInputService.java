package com.codementor.question.service;

import com.codementor.question.dto.request.PlanInputRequest;
import com.codementor.question.entity.Plan;
import com.codementor.question.entity.PlanMap;
import com.codementor.question.repository.PlanMap.PlanMapRepository;
import com.codementor.question.repository.Plan.PlanRepository;
import com.codementor.question.repository.Question.QuestionRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanInputService {
    private final PlanMapRepository planMapRepository;
    private final PlanRepository planRepository;
    private final QuestionRepositorySupport questionRepositorySupport;

    public Long planInput(PlanInputRequest request) {
        var plan = planRepository.save(Plan.from(request));
        List<PlanMap> planMaps = request.getQuestionIds().stream()
                .map(questionId -> PlanMap.builder()
                        .plan(plan)
                        .question(questionRepositorySupport.findById(questionId).orElseThrow())
                        .build())
                .collect(Collectors.toList());
        planMapRepository.saveAll(planMaps);
        return plan.getId();
    }
}
