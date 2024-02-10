package com.codementor.question.service;


import com.codementor.question.core.util.RequestToServer;
import com.codementor.question.entity.Question;
import com.codementor.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    @Value("${server.execute.url}")
    private String executeUrl;

    private final RequestToServer requestToServer;

    private final QuestionRepository questionRepository;

    public Map<String, Long> getQuestionInfoByQuestionIdList(Long userId) {
        String url = executeUrl + "/api/external/correct-user-question-id-list";
        List<Long> correctUserQuestionIdList = requestToServer.postDataToServer(url, userId, ArrayList.class);

        Map<String, Long> questionCountByTag = new HashMap<>();
        for (var questionId : correctUserQuestionIdList) {
            Question question = questionRepository.findById(questionId).orElse(null);
            if (question != null) {
                String category = question.getCategory();
                questionCountByTag.put(category, questionCountByTag.getOrDefault(category, 0L) + 1);
            }
        }
        return questionCountByTag;
    }

}
