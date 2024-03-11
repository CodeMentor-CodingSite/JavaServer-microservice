package com.codementor.question.service;

import com.codementor.question.dto.external.*;
import com.codementor.question.entity.*;
import com.codementor.question.enums.QuestionDifficulty;
import com.codementor.question.repository.Language.LanguageRepositorySupport;
import com.codementor.question.repository.Plan.PlanRepository;
import com.codementor.question.repository.Question.QuestionRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 해당 클래스는 execution 서버가 kafka에 EvaluationDto를 전송해야할때 필요한 Question 데이터 일부를 가공하는 클래스.
 *
 */
@Service
@RequiredArgsConstructor
public class ExecutionHelperService {
    private final QuestionRepositorySupport questionRepositorySupport;
    private final LanguageRepositorySupport languageRepositorySupport;
    private final PlanRepository planRepository;


    public EvaluationDto createEvaluationDto(Long questionId, String userLanguage) {

        Language language = languageRepositorySupport.findByType(userLanguage)
                .orElseThrow(() -> new EntityNotFoundException("Language with type " + userLanguage + " not found"));
        Question question = questionRepositorySupport.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question with ID " + questionId + " not found"));

        List<String> questionConstraints = question.getQuestionConstraints().stream()
                .map(QuestionConstraint::getContent)
                .collect(Collectors.toList());

        List<EvalTestCaseDto> testCaseDtoList = question.getQuestionTestCases().stream()
                .map(testCase -> EvalTestCaseDto.from(testCase, createTestCaseDetailAndConverterDtoList(testCase, language)))
                .collect(Collectors.toList());

        String answerCheckContent = question.getQuestionLanguages().stream()
                .filter(ql -> ql.getLanguage().equals(language))
                .findFirst()
                .map(QuestionLanguage::getCheckContent)
                .orElse(null);

        //builder
        return EvaluationDto.from(question, questionConstraints, testCaseDtoList, answerCheckContent);
    }

    private List<EvalTestCaseDetailAndConverterDto> createTestCaseDetailAndConverterDtoList(QuestionTestCase questionTestCase, Language language){
        List<EvalTestCaseDetailAndConverterDto> resultList = new ArrayList<>();

        // QuestionTestCase와 연결된 QuestionTestCaseDetail들을 순회
        for (QuestionTestCaseDetail testCaseDetail : questionTestCase.getQuestionTestCaseDetails()) {
            var tcAndConv = EvalTestCaseDetailAndConverterDto.from(testCaseDetail);

            // 각 QuestionTestCaseDetail에 연결된 ConverterMap들 중에서 해당 Language에 해당하는 Converter 찾기
            for (ConverterMap converterMap : testCaseDetail.getConverterMaps()) {
                CodeExecConverter converter = converterMap.getCodeExecConverter();

                // 해당 Language와 연결된 Converter만 처리
                if (converter!=null && converter.getLanguage().equals(language)) {
                    tcAndConv.updateWith(converter);
                }
            }
            resultList.add(tcAndConv);
        }
        return resultList;
    }

    public QuestionDifficultyCounts getAllQuestionsDifficultyCounts(){
        List<Question> questionsList = questionRepositorySupport.findAll();
        return QuestionDifficultyCounts.fromQuestionList(questionsList);
    }

    public UserSolvedRatioTotalDto getUserSolvedRatioSubmitDto(UserSolvedRatioTotalDto req){
        return req.updatedWith(req, questionRepositorySupport.findAll());
    }

    public UserSolvedCategoryDtoList getUserSolvedCategoryQuestionList(UserSolvedQuestionIdList req){
        Set<Long> problemIdSet = new HashSet<>(req.getProblemIdList());
        List<Question> questionsList = questionRepositorySupport.findAllById(problemIdSet);
        List<UserSolvedCategoryDto> result = questionsList.stream()
                .map(question -> new UserSolvedCategoryDto().from(question))
                .collect(Collectors.toList());
        return UserSolvedCategoryDtoList.builder()
                .userSolvedCategoryDtoList(result)
                .build();
    }

    public List<UserSolvedQuestionIdAndTitleAndTimeResponse> getQuestionNameFromId(List<UserSolvedQuestionIdAndTitleAndTimeResponse> request) {
        List<UserSolvedQuestionIdAndTitleAndTimeResponse> newResponse = new ArrayList<>();
        for (var req : request) {
            Optional<Question> question = questionRepositorySupport.findByIdAndDifficulty(req.getQuestionId(), QuestionDifficulty.valueOf(req.getDifficulty()));
            if (question.isPresent()){
                newResponse.add(UserSolvedQuestionIdAndTitleAndTimeResponse.of(req, question));
            }
        }
        return newResponse;
    }

    public List<UserSubmitHistoryResponse> getUserSubmitHistory(List<UserSubmitHistoryResponse> request){
        for (var req : request) {
            Optional<Question> question = questionRepositorySupport.findById(req.getQuestionId());
            if (question.isPresent()){
                req.setQuestionName(question.get().getTitle());
            }
        }
        return request;
    }

    public List<UserPlanDto> getUserPlan(List<Long> userPlanIds) {
        return planRepository.findAllById(userPlanIds)
                .stream()
                .map(UserPlanDto::from)
                .collect(Collectors.toList());
    }
}
