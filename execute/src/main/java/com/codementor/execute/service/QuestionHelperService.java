package com.codementor.execute.service;

import com.codementor.execute.dto.external.UserQuestionsStatus;
import com.codementor.execute.entity.ExecuteUsercode;
import com.codementor.execute.repository.ExecuteUsercodeRepository;
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
