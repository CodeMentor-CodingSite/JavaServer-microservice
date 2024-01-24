package com.codementor.question.service;

import com.codementor.question.dto.external.UserQuestionsStatus;
import com.codementor.question.dto.PlanDto;
import com.codementor.question.dto.response.PlanResponse;
import com.codementor.question.dto.response.QuestionDetailDtoResponse;
import com.codementor.question.dto.QuestionDto;
import com.codementor.question.dto.response.QuestionInitCodeResponse;
import com.codementor.question.entity.Question;
import com.codementor.question.entity.QuestionLanguage;
import com.codementor.question.enums.UserSolvedStatus;
import com.codementor.question.mapper.QuestionMapper;
import com.codementor.question.repository.PlanMapRepository;
import com.codementor.question.repository.PlanRepository;
import com.codementor.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    @Value("$server.execute.url")
    private String executeUrl;

    private final QuestionRepository questionRepository;
    private final PlanRepository planRepository;
    private final PlanMapRepository planMapRepository;

    private RestTemplate restTemplate;

    /**
     * 페이지네이션을 통해 문제를 불러오고, 유저가 푼 문제인지에 대한 status를 포함한 dto 반환
     * @param userId 유저 아이디
     * @param pageable 페이지네이션 정보
     * @return 페이지네이션된 문제 dto
     */
    public Page<QuestionDto> getPaginatedQuestionDtos(Long userId, Pageable pageable){
        UserQuestionsStatus userQuestionsStatus = getuserQuestionsStatus(executeUrl + "/api/external/question/get/user/status", userId);
        HashSet<Long> userSolvedQuestionIdsSet = new HashSet<>(userQuestionsStatus.getAttmptedQuestions());
        HashSet<Long> userAttemptedQuestionIdsSet = new HashSet<>(userQuestionsStatus.getSolvedQuestions());

        Page<Question> questionPage = questionRepository.findAll(pageable);
        List<QuestionDto> questionDtos = new ArrayList<>();

        for (Question question : questionPage.getContent()) {
            UserSolvedStatus userSolvedStatus = UserSolvedStatus.FIRST;
            if (userSolvedQuestionIdsSet.contains(question.getId())) {
                userSolvedStatus = UserSolvedStatus.SOLVED;
            } else if (userAttemptedQuestionIdsSet.contains(question.getId())) {
                userSolvedStatus = UserSolvedStatus.ATTEMPTED;
            }
            questionDtos.add(QuestionDto.from(question, userSolvedStatus));
        }

        return new PageImpl<>(questionDtos, pageable, questionPage.getTotalElements());
    }

    private UserQuestionsStatus getuserQuestionsStatus(String url, Long userId) {
        ResponseEntity<UserQuestionsStatus> response = restTemplate.postForEntity(url, userId, UserQuestionsStatus.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to get valid response from " + url);
        }
    }

    /**
     * 문제를 id로 조회하여 해당 entity를 dto로 변환하여 반환
     * language는 entity가 아닌 List<String> 값으로 반환
     * @param questionId 문제 아이디
     * @return 문제 dto
     */
    public QuestionDetailDtoResponse getQuestionById(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new RuntimeException("Question not found"));
        return QuestionMapper.toDto(question);
    }

    /**
     * 문제의 초기 코드를 반환
     * @param questionId 문제 아이디
     * @param language 언어
     * @return 초기 코드
     */
    public QuestionInitCodeResponse getQuestionInitialCode(Long questionId, String language) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        QuestionLanguage questionLanguage = question.getQuestionLanguages().stream()
                .filter(ql -> ql.getLanguage()!= null && language.equals(ql.getLanguage().getType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Question language not found"));
        return new QuestionInitCodeResponse(questionLanguage.getInitContent());
    }

    /**
     * PlanEntity를 PlanDto로 변환하여 반환
     * @return PlanResponse
     */
    public PlanResponse getAllPlans() {
        return new PlanResponse(planRepository.findAll());
    }
}
