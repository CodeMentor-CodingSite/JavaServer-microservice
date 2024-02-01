package com.codementor.question.service;


import com.codementor.question.core.exception.CodeMentorException;
import com.codementor.question.core.exception.ErrorEnum;
import com.codementor.question.dto.request.ConverterInputRequest;
import com.codementor.question.dto.request.QuestionCodeInputRequest;
import com.codementor.question.dto.request.QuestionInputRequest;
import com.codementor.question.dto.request.TestCaseRequest;
import com.codementor.question.entity.*;
import com.codementor.question.repository.*;
import jdk.jshell.spi.ExecutionControl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionInputService {
    private final QuestionRepository questionRepository;
    private final LanguageRepository languageRepository;
    private final QuestionLanguageRepository questionLanguageRepository;
    private final QuestionTestCaseRepository questionTestCaseRepository;
    private final QuestionTestCaseDetailRepository questionTestCaseDetailRepository;
    private final CodeExecConverterRepository codeExecConverterRepository;
    private final QuestionConstraintRepository questionConstraintRepository;
    private final ConverterMapRepository converterMapRepository;

    /**
     * 문제 입력
     * @param request 문제 입력 요청
     * @return 문제 ID
     */
    @Transactional
    public Long questionInput(QuestionInputRequest request) {
        Question question = Question.builder()
                .title(request.getQuestionTitle())
                .content(request.getQuestionContent())
                .category(request.getQuestionCategory())
                .build();
        Question savedQuestion = questionRepository.save(question);

        for (int i = 0; i < request.getQuestionConstraintContents().size(); i++) {
            QuestionConstraint questionConstraint = QuestionConstraint.builder()
                    .question(savedQuestion)
                    .content(request.getQuestionConstraintContents().get(i))
                    .build();
            questionConstraintRepository.save(questionConstraint);
        }

        return savedQuestion.getId();
    }

    /**
     * 문제 테스트 케이스 입력
     * @param testCaseRequest 문제 코드 입력 요청
     * @return 문제 테스트 케이스 ID
     */
    @Transactional
    public Long testCaseInput(TestCaseRequest testCaseRequest) {
        Question question = questionRepository.findById((long) testCaseRequest.getQuestionId()).orElseThrow();

        QuestionTestCase questionTestCase = QuestionTestCase.builder()
                .question(question)
                .isExample(testCaseRequest.getIsExample())
                .explanation(testCaseRequest.getExplanation())
                .build();
        QuestionTestCase savedQuestionTestCase = questionTestCaseRepository.save(questionTestCase);

        // 각 TestCaseDetail에 ConverterMap 저장
        for (int i = 0; i < testCaseRequest.getTestCaseDetailDTOs().size(); i++) {
            QuestionTestCaseDetail questionTestCaseDetail = QuestionTestCaseDetail.builder()
                    .questionTestCase(savedQuestionTestCase)
                    .key(testCaseRequest.getTestCaseDetailDTOs().get(i).getTestCaseKey())
                    .value(testCaseRequest.getTestCaseDetailDTOs().get(i).getTestCaseValue())
                    .build();
            questionTestCaseDetailRepository.save(questionTestCaseDetail);

            ArrayList<Long> converterIds = testCaseRequest.getTestCaseDetailDTOs().get(i).getConverterIds();

            // 각 TestCaseDetailId와 ConverterId를 ConverterMap에 저장
            for (Long converterId : converterIds) {
                CodeExecConverter codeExecConverter = codeExecConverterRepository.findById(converterId).orElseThrow();
                ConverterMap converterMap = ConverterMap.builder()
                        .questionTestCaseDetail(questionTestCaseDetail)
                        .codeExecConverter(codeExecConverter)
                        .build();
                converterMapRepository.save(converterMap);
            }
        }

        return savedQuestionTestCase.getId();
    }

    /**
     * 코드 컨버터 입력
     * @param converterInputRequest 코드 컨버터 입력 요청
     * @return 코드 컨버터 ID
     */
    @Transactional
    public Long converterInput(ConverterInputRequest converterInputRequest) {
        Language languageEntity = languageRepository.findByType(converterInputRequest.getLanguageType()).orElseThrow();

        CodeExecConverter codeExecConverter = CodeExecConverter.builder()
                .language(languageEntity)
                .content(converterInputRequest.getCodeExecConverterContent())
                .returnType(converterInputRequest.getResultType())
                .methodName(converterInputRequest.getMethodName())
                .build();

        CodeExecConverter savedCodeExecConverter = codeExecConverterRepository.save(codeExecConverter);
        return savedCodeExecConverter.getId();
    }

    /**
     * 문제 초기 코드 입력
     * @param questionCodeInputRequest 문제 초기 코드 입력 요청
     * @return 문제 초기 코드 ID
     */
    @Transactional
    public Long questionCodeInput(QuestionCodeInputRequest questionCodeInputRequest) {
        Question question = questionRepository.findById(questionCodeInputRequest.getQuestionId())
                .orElseThrow(() -> new CodeMentorException(ErrorEnum.RECORD_NOT_FOUND));
        Language language = languageRepository.findByType(questionCodeInputRequest.getLanguageType())
                .orElseThrow(() -> new CodeMentorException(ErrorEnum.RECORD_NOT_FOUND));
        Optional<QuestionLanguage> foundQuestionLanguage = questionLanguageRepository.findByQuestionAndLanguage(question, language);
        if (foundQuestionLanguage.isPresent()) {
            throw new CodeMentorException(ErrorEnum.RECORD_ALREADY_EXISTS);
        }

        QuestionLanguage questionLanguage = QuestionLanguage.builder()
                .question(question)
                .language(language)
                .initContent(questionCodeInputRequest.getQuestionInitContent())
                .checkContent(questionCodeInputRequest.getAnswerCheckContent())
                .build();

        QuestionLanguage response = questionLanguageRepository.save(questionLanguage);
        return response.getId();
    }

}
