package com.codementor.question.service;

import com.codementor.question.dto.external.*;
import com.codementor.question.entity.*;
import com.codementor.question.enums.QuestionDifficulty;
import com.codementor.question.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 해당 클래스는 execution 서버가 kafka에 EvaluationDto를 전송해야할때 필요한 Question 데이터 일부를 가공하는 클래스.
 *
 */
@Service
@RequiredArgsConstructor
public class ExecutionHelperService {
    private final QuestionRepository questionRepository;
    private final LanguageRepository languageRepository;
    private final QuestionLanguageRepository questionLanguageRepository;
    private final QuestionTestCaseRepository questionTestCaseRepository;


    public EvaluationDto createEvaluationDto(Long questionId, String userLanguage) {

        Language language = languageRepository.findByType(userLanguage)
                .orElseThrow(() -> new EntityNotFoundException("Language with type " + userLanguage + " not found"));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question with ID " + questionId + " not found"));

        String questionTitle = question.getTitle();
        String questionContent = question.getContent();
        String questionCategory = question.getCategory();
        List<String> questionConstraints = new ArrayList<>();
        for (QuestionConstraint qc : question.getQuestionConstraints()) {
            questionConstraints.add(qc.getContent());
        }
        List<EvalTestCaseDto> testCaseDtoList = createTestCaseDtoList(question, language);
        String answerCheckContent = getAnswerCheckContent(question, language);

        //builder
        return EvaluationDto.builder()
                .questionId(questionId)
                .questionTitle(questionTitle)
                .questionContent(questionContent)
                .questionCategory(questionCategory)
                .questionConstraints(questionConstraints)
                .testCaseDtoList(testCaseDtoList)
                .answerCheckContent(answerCheckContent)
                .build();
    }

    private String getAnswerCheckContent(Question question, Language language) {
        Optional<QuestionLanguage> questionLanguage = questionLanguageRepository.findByQuestionAndLanguage(question, language);
        return questionLanguage.map(QuestionLanguage::getCheckContent).orElse(null);
    }

    private List<EvalTestCaseDto> createTestCaseDtoList(Question question, Language language) {
        List<EvalTestCaseDto> testCaseDtoList = new ArrayList<>();
        List<QuestionTestCase> questionTestCaseList = questionTestCaseRepository.findByQuestion(question);

        // QuestionTestCase들을 순회
        for (QuestionTestCase testCase : questionTestCaseList) {
            EvalTestCaseDto testCaseDto = EvalTestCaseDto.builder()
                    .testCaseId(testCase.getId())
                    .isExample(testCase.getIsExample())
                    .explanation(testCase.getExplanation())
                    // 각 QuestionTestCase에 연결된 QuestionTestCaseDetail들을 순회하며 해당 Language에 해당하는 Converter를 찾아 TestCaseDetailAndConverterDto를 생성
                    .evalTestCaseDetailAndConverterDtos(createTestCaseDetailAndConverterDtoList(testCase, language))
                    .testCaseResult("processing")
                    .build();
            testCaseDtoList.add(testCaseDto);
        }
        return testCaseDtoList;
    }

    private List<EvalTestCaseDetailAndConverterDto> createTestCaseDetailAndConverterDtoList(QuestionTestCase questionTestCase, Language language){
        List<EvalTestCaseDetailAndConverterDto> resultList = new ArrayList<>();

        // QuestionTestCase와 연결된 QuestionTestCaseDetail들을 순회
        for (QuestionTestCaseDetail testCaseDetail : questionTestCase.getQuestionTestCaseDetails()) {
            EvalTestCaseDetailAndConverterDto tcAndConv = new EvalTestCaseDetailAndConverterDto();
            tcAndConv.setTestCaseKey(testCaseDetail.getKey());
            tcAndConv.setTestCaseValue(testCaseDetail.getValue());

            // 각 QuestionTestCaseDetail에 연결된 ConverterMap들 중에서 해당 Language에 해당하는 Converter 찾기
            for (ConverterMap converterMap : testCaseDetail.getConverterMaps()) {
                CodeExecConverter converter = converterMap.getCodeExecConverter();

                // 해당 Language와 연결된 Converter만 처리
                if (converter!=null && converter.getLanguage().equals(language)) {
                    tcAndConv.setCodeExecConverterContent(converter.getContent());
                    tcAndConv.setReturnType(converter.getReturnType());
                    tcAndConv.setMethodName(converter.getMethodName());
                }
            }
            resultList.add(tcAndConv);
        }
        return resultList;
    }

    public QuestionDifficultyCounts getAllQuestionsDifficultyCounts(){
        List<Question> questionsList = questionRepository.findAll();
        Long easy = 0L;
        Long medium = 0L;
        Long hard = 0L;

        for (Question question : questionsList) {
            switch (question.getDifficulty()) {
                case EASY:
                    easy++;
                    break;
                case MEDIUM:
                    medium++;
                    break;
                case HARD:
                    hard++;
                    break;
            }
        }
        return QuestionDifficultyCounts.builder()
                .easyProblemsCount(easy)
                .mediumProblemsCount(medium)
                .hardProblemsCount(hard)
                .build();
    }

    public UserSolvedRatioTotalDto getUserSolvedRatioSubmitDto(UserSolvedRatioTotalDto req){
        Long easyProblemSolvedCount = 0L;
        Long mediumProblemSolvedCount = 0L;
        Long hardProblemSolvedCount = 0L;

        // make a hashset for unique user problem list
        HashSet<Long> questionSolvedList = new HashSet<>();
        questionSolvedList.addAll(req.getQuestionIdList());
        List<Question> questionsList = questionRepository.findAll();
        for (Question question : questionsList) {
            if (questionSolvedList.contains(question.getId())) {
                switch (question.getDifficulty()) {
                    case EASY:
                        easyProblemSolvedCount++;
                        break;
                    case MEDIUM:
                        mediumProblemSolvedCount++;
                        break;
                    case HARD:
                        hardProblemSolvedCount++;
                        break;
                }
            }
        }
        return req.addProblemSolvedCounts(easyProblemSolvedCount, mediumProblemSolvedCount, hardProblemSolvedCount);
    }

    public UserSolvedCategoryDtoList getUserSolvedCategoryQuestionList(UserSolvedQuestionIdList req){
        Set<Long> problemIdSet = new HashSet<>(req.getProblemIdList());
        List<Question> questionsList = questionRepository.findAllById(problemIdSet);
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
            Optional<Question> question = questionRepository.findByIdAndDifficulty(req.getQuestionId(), QuestionDifficulty.valueOf(req.getDifficulty()));
            if (question.isPresent()){
                newResponse.add(UserSolvedQuestionIdAndTitleAndTimeResponse.builder()
                                .usercodeId(req.getUsercodeId())
                                .questionId(question.get().getId())
                                .questionTitle(question.get().getTitle())
                                .difficulty(req.getDifficulty())
                                .timeStamp(req.getTimeStamp())
                                .build());
            }
        }
        return newResponse;
    }
}
