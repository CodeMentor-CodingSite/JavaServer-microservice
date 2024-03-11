package com.codementor.question.service;


import com.codementor.question.core.exception.CodeMentorException;
import com.codementor.question.core.exception.ErrorEnum;
import com.codementor.question.dto.request.ConverterInputRequest;
import com.codementor.question.dto.request.QuestionCodeInputRequest;
import com.codementor.question.dto.request.QuestionInputRequest;
import com.codementor.question.dto.request.TestCaseRequest;
import com.codementor.question.entity.*;
import com.codementor.question.repository.CodeExecConverter.CodeExecConverterRepositorySupport;
import com.codementor.question.repository.ConverterMap.ConverterMapRepositorySupport;
import com.codementor.question.repository.Language.LanguageRepositorySupport;
import com.codementor.question.repository.Question.QuestionRepositorySupport;
import com.codementor.question.repository.QuestionConstraint.QuestionConstraintRepository;
import com.codementor.question.repository.QuestionLanguage.QuestionLanguageRepositorySupport;
import com.codementor.question.repository.QuestionTestCase.QuestionTestCaseRepository;
import com.codementor.question.repository.QuestionTestCaseDetail.QuestionTestCaseDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionInputService {
    private final QuestionRepositorySupport questionRepositorySupport;
    private final LanguageRepositorySupport languageRepositorySupport;
    private final QuestionLanguageRepositorySupport questionLanguageRepositorySupport;
    private final QuestionTestCaseRepository questionTestCaseRepository;
    private final QuestionTestCaseDetailRepository questionTestCaseDetailRepository;
    private final CodeExecConverterRepositorySupport codeExecConverterRepositorySupport;
    private final QuestionConstraintRepository questionConstraintRepository;
    private final ConverterMapRepositorySupport converterMapRepositorySupport;

    /**
     * 문제 입력
     * @param request 문제 입력 요청
     * @return 문제 ID
     */
    @Transactional
    public Long questionInput(QuestionInputRequest request) {
        Question savedQuestion = questionRepositorySupport.save(Question.from(request));
        for (int i = 0; i < request.getQuestionConstraintContents().size(); i++) {
            questionConstraintRepository.save(QuestionConstraint.from(savedQuestion, request.getQuestionConstraintContents().get(i)));
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
        var question = questionRepositorySupport.findById((long) testCaseRequest.getQuestionId()).orElseThrow();
        var savedQuestionTestCase = questionTestCaseRepository.save(QuestionTestCase.from(question, testCaseRequest));

        // 각 TestCaseDetail에 ConverterMap 저장
        for (int i = 0; i < testCaseRequest.getTestCaseDetailDTOs().size(); i++) {
            var questionTestCaseDetail = QuestionTestCaseDetail.from(
                    savedQuestionTestCase,
                    testCaseRequest.getTestCaseDetailDTOs().get(i).getTestCaseKey(),
                    testCaseRequest.getTestCaseDetailDTOs().get(i).getTestCaseValue());
            questionTestCaseDetailRepository.save(questionTestCaseDetail);

            ArrayList<Long> converterIds = testCaseRequest.getTestCaseDetailDTOs().get(i).getConverterIds();

            for (Long converterId : converterIds) { // // 각 TestCaseDetailId와 ConverterId를 ConverterMap에 저장
                converterMapRepositorySupport.save(ConverterMap.from(questionTestCaseDetail, codeExecConverterRepositorySupport.findById(converterId).orElseThrow()));
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
        var languageEntity = languageRepositorySupport.findByType(converterInputRequest.getLanguageType()).orElseThrow();
        return codeExecConverterRepositorySupport.save(CodeExecConverter.from(languageEntity, converterInputRequest)).getId();
    }

    /**
     * 문제 초기 코드 입력
     * @param questionCodeInputRequest 문제 초기 코드 입력 요청
     * @return 문제 초기 코드 ID
     */
    @Transactional
    public Long questionCodeInput(QuestionCodeInputRequest questionCodeInputRequest) {
        var question = questionRepositorySupport.findById(questionCodeInputRequest.getQuestionId()).orElseThrow(
                () -> new CodeMentorException(ErrorEnum.RECORD_NOT_FOUND));
        var language = languageRepositorySupport.findByType(questionCodeInputRequest.getLanguageType()).orElseThrow(
                () -> new CodeMentorException(ErrorEnum.RECORD_NOT_FOUND));

        Optional<QuestionLanguage> foundQuestionLanguage = questionLanguageRepositorySupport.findByQuestionAndLanguage(question, language);
        if (foundQuestionLanguage.isPresent()) {
            throw new CodeMentorException(ErrorEnum.RECORD_ALREADY_EXISTS);
        }
        return questionLanguageRepositorySupport.save(QuestionLanguage.from(question, language, questionCodeInputRequest)).getId();
    }

}
