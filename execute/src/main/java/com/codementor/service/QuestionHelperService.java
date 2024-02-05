package com.codementor.service;

import com.codementor.dto.external.UserQuestionsStatus;
import com.codementor.entity.ExecuteUsercode;
import com.codementor.repository.ExecuteUsercodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class QuestionHelperService {
    private final ExecuteUsercodeRepository executeUsercodeRepository;

    /**
     * 유저 아이디로 유저가 푼 문제와 시도한 문제를 가져온다.
     * @param userId
     * @return 유저가 시도한 문제 Id 리스트와 유저가 푼 문제 Id 리스트
     */
    public UserQuestionsStatus getUserQuestionsStatus(Long userId) {
        System.out.println("userId: " + userId);
        List<ExecuteUsercode> executeUsercodes = executeUsercodeRepository.findAllByUserId(userId);
        Set<Long> attemptedQuestionIds = new HashSet<>();
        Set<Long> solvedQuestionIds = new HashSet<>();

        for (ExecuteUsercode executeUsercode : executeUsercodes) {
            if (executeUsercode.getIsCorrect()) {
                solvedQuestionIds.add(executeUsercode.getQuestionId());
            } else {
                attemptedQuestionIds.add(executeUsercode.getQuestionId());
            }
        }
        return UserQuestionsStatus.builder()
                .attmptedQuestions(new ArrayList<>(attemptedQuestionIds))
                .solvedQuestions(new ArrayList<>(solvedQuestionIds))
                .build();
    }

    public List<Long> getCorrectUserQuestionIdList(Long userId) {
        List<ExecuteUsercode> executeUsercodes = executeUsercodeRepository.findAllByUserId(userId);
        HashSet<Long> correctUserQuestionIdList = new HashSet<>();
        for (ExecuteUsercode executeUsercode : executeUsercodes) {
            if (executeUsercode.getIsCorrect()) {
                correctUserQuestionIdList.add(executeUsercode.getQuestionId());
            }
        }
        return new ArrayList<Long>(correctUserQuestionIdList);
    }
}
