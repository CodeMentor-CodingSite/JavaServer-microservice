package com.codementor.question.service;

import com.codementor.question.core.exception.CodeMentorException;
import com.codementor.question.core.exception.ErrorEnum;
import com.codementor.question.core.util.RequestToServer;
import com.codementor.question.dto.external.UserQuestionsStatus;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    @Value("${server.execute.url}")
    private String executeUrl;

    private final RequestToServer requestToServer;

    private final QuestionRepository questionRepository;
    private final PlanRepository planRepository;
    private final PlanMapRepository planMapRepository;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 페이지네이션을 통해 문제를 불러오고, 유저가 푼 문제인지에 대한 status를 포함한 dto 반환
     * @param userId 유저 아이디
     * @param pageable 페이지네이션 정보
     * @return 페이지네이션된 문제 dto
     */
    public Page<QuestionDto> getPaginatedQuestionDtos(Long userId, Pageable pageable){
        Page<Question> questionPage = questionRepository.findAll(pageable);

        var getUserQuestionsStatusUrl = executeUrl + "/api/external/question/get/user/status";
        var userQuestionsStatus = requestToServer.postDataToServer(getUserQuestionsStatusUrl, userId, UserQuestionsStatus.class);

        System.out.println("userQuestionsStatus: " + userQuestionsStatus.toString());
        return new PageImpl<>(QuestionDto.from(questionPage, userQuestionsStatus), pageable, questionPage.getTotalElements());
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
        return QuestionMapper.toDto(questionRepository.findById(questionId).orElseThrow(
                () -> new RuntimeException("Question not found")));
    }

    /**
     * 문제의 초기 코드를 반환
     * @param questionId 문제 아이디
     * @param language 언어
     * @return 초기 코드
     */
    public QuestionInitCodeResponse getQuestionInitialCode(Long questionId, String language) {
        var question = questionRepository.findById(questionId)
                .orElseThrow(() -> new CodeMentorException(ErrorEnum.RECORD_NOT_FOUND));
        var questionLanguage = question.getQuestionLanguages().stream()
                .filter(ql -> ql.getLanguage()!= null && language.equals(ql.getLanguage().getType()))
                .findFirst()
                .orElseThrow(() -> new CodeMentorException(ErrorEnum.RECORD_NOT_FOUND));
        return new QuestionInitCodeResponse(questionLanguage.getInitContent());
    }
}
