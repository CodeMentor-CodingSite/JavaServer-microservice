package com.codementor.service;

import com.codementor.dto.external.UserQuestionsStatus;
import com.codementor.entity.ExecuteUsercode;
import com.codementor.repository.ExecuteUsercode.ExecuteUsercodeRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionHelperService {
    private final ExecuteUsercodeRepositorySupport executeUsercodeRepositorySupport;

    /**
     * 유저 아이디로 유저가 푼 문제와 시도한 문제를 가져온다.
     * @param userId
     * @return 유저가 시도한 문제 Id 리스트와 유저가 푼 문제 Id 리스트
     */
    public UserQuestionsStatus getUserQuestionsStatus(Long userId) {
        return UserQuestionsStatus.from(executeUsercodeRepositorySupport.findAllByUserId(userId));
    }

    public List<Long> getCorrectUserQuestionIdList(Long userId) {
        List<ExecuteUsercode> executeUsercodes = executeUsercodeRepositorySupport.findAllByUserId(userId);
        HashSet<Long> correctUserQuestionIdList = new HashSet<>();
        for (ExecuteUsercode executeUsercode : executeUsercodes) {
            if (executeUsercode.getIsCorrect()) {
                correctUserQuestionIdList.add(executeUsercode.getQuestionId());
            }
        }
        return new ArrayList<Long>(correctUserQuestionIdList);
    }
}
