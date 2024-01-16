package com.codementor.question.service;

import com.codementor.question.dto.evaluation.EvaluationDto;
import com.codementor.question.dto.evaluation.EvalTestCaseDetailAndConverterDto;
import com.codementor.question.dto.evaluation.EvalTestCaseDto;
import com.codementor.question.entity.*;
import com.codementor.question.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final QuestionTestCaseDetailRepository questionTestCaseDetailRepository;
    private final CodeExecConverterRepository codeExecConverterRepository;
    private final QuestionConstraintRepository questionConstraintRepository;
    private final ConverterMapRepository converterMapRepository;


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
        List<EvalTestCaseDto> testCaseDtoList = createTestCaseDetailDtoList(question, language);
        String answerCheckContent = getAnswerCheckContent(question, language);

        //builder
        EvaluationDto evaluationDto = EvaluationDto.builder()
                .questionId(questionId)
                .questionTitle(questionTitle)
                .questionContent(questionContent)
                .questionCategory(questionCategory)
                .questionConstraints(questionConstraints)
                .testCaseDtoList(testCaseDtoList)
                .answerCheckContent(answerCheckContent)
                .build();

        return evaluationDto;
    }

    private String getAnswerCheckContent(Question question, Language language) {
        Optional<QuestionLanguage> questionLanguage = questionLanguageRepository.findByQuestionAndLanguage(question, language);
        return questionLanguage.map(QuestionLanguage::getCheckContent).orElse(null);
    }

    private List<EvalTestCaseDto> createTestCaseDetailDtoList(Question question, Language language) {
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
                    .build();
            testCaseDtoList.add(testCaseDto);
        }
        return testCaseDtoList;
    }


    private List<EvalTestCaseDetailAndConverterDto> createTestCaseDetailAndConverterDtoList(QuestionTestCase questionTestCase, Language language){
        List<EvalTestCaseDetailAndConverterDto> resultList = new ArrayList<>();

        // QuestionTestCase와 연결된 QuestionTestCaseDetail들을 순회
        for (QuestionTestCaseDetail testCaseDetail : questionTestCase.getQuestionTestCaseDetails()) {

            // 각 QuestionTestCaseDetail에 연결된 ConverterMap들 중에서 해당 Language에 해당하는 Converter 찾기
            for (ConverterMap converterMap : testCaseDetail.getConverterMaps()) {
                CodeExecConverter converter = converterMap.getCodeExecConverter();

                // 해당 Language와 연결된 Converter만 처리
                if (converter!=null && converter.getLanguage().equals(language)) {
                    EvalTestCaseDetailAndConverterDto tcAndConv = EvalTestCaseDetailAndConverterDto.builder()
                            .codeExecConverterContent(converter.getContent())
                            .returnType(converter.getReturnType())
                            .methodName(converter.getMethodName())
                            .testCaseKey(testCaseDetail.getKey())
                            .testCaseValue(testCaseDetail.getValue())
                            .build();
                    resultList.add(tcAndConv);
                }
            }
        }
        return resultList;
    }
}