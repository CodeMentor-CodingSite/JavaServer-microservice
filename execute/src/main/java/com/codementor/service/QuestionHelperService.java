package com.codementor.service;

import com.codementor.dto.external.UserQuestionsStatus;
import com.codementor.entity.ExecuteUsercode;
import com.codementor.repository.ExecuteUsercodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionHelperService {
    private final ExecuteUsercodeRepository executeUsercodeRepository;

    public UserQuestionsStatus getUserQuestionsStatus(Long userId) {
        List<ExecuteUsercode> executeUsercodes = executeUsercodeRepository.findAllByUserId(userId);
        List<Long> attemptedQuestionIds = null;
        List<Long> solvedQuestionIds = null;

        for (ExecuteUsercode executeUsercode : executeUsercodes) {
            if (executeUsercode.getIsCorrect()) {
                solvedQuestionIds.add(executeUsercode.getQuestionId());
            } else {
                attemptedQuestionIds.add(executeUsercode.getQuestionId());
            }
        }
        return new UserQuestionsStatus(attemptedQuestionIds, solvedQuestionIds);
    }
}
