package com.codementor.question.service;


import com.codementor.question.dto.request.ConverterInputRequest;
import com.codementor.question.dto.request.QuestionCodeInputRequest;
import com.codementor.question.dto.request.QuestionInputRequest;
import com.codementor.question.dto.request.TestCaseRequest;
import com.codementor.question.entity.*;
import com.codementor.question.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

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

    @Transactional
    public Integer questionInput(QuestionInputRequest request) {
        Question question = Question.builder()
                .title(request.getQuestionTitle())
                .content(request.getQuestionContent())
                .category(request.getQuestionCategory())
                .build();
        Question response = questionRepository.save(question);

        for (int i = 0; i < request.getQuestionConstraintContents().size(); i++) {
            QuestionConstraint questionConstraint = QuestionConstraint.builder()
                    .question(response)
                    .content(request.getQuestionConstraintContents().get(i))
                    .build();
            questionConstraintRepository.save(questionConstraint);
        }

        return response.getId().intValue();
    }

    @Transactional
    public Integer testCaseInput(TestCaseRequest request) {
        Question question = questionRepository.findById((long) request.getQuestionId()).orElseThrow();

        QuestionTestCase questionTestCase = QuestionTestCase.builder()
                .question(question)
                .isExample(request.getIsExample())
                .explanation(request.getExplanation())
                .build();
        QuestionTestCase questionTestCaseResponse = questionTestCaseRepository.save(questionTestCase);

        // 각 TestCaseDetail에 ConverterMap 저장
        for (int i = 0; i < request.getTestCaseDetailDTOs().size(); i++) {
            QuestionTestCaseDetail questionTestCaseDetail = QuestionTestCaseDetail.builder()
                    .questionTestCase(questionTestCaseResponse)
                    .key(request.getTestCaseDetailDTOs().get(i).getTestCaseKey())
                    .value(request.getTestCaseDetailDTOs().get(i).getTestCaseValue())
                    .build();
            questionTestCaseDetailRepository.save(questionTestCaseDetail);

            ArrayList<Integer> converterIds = request.getTestCaseDetailDTOs().get(i).getConverterIds();

            // 각 TestCaseDetailId와 ConverterId를 ConverterMap에 저장
            for (Integer converterId : converterIds) {
                CodeExecConverter codeExecConverter = codeExecConverterRepository.findById((long) converterId).orElseThrow();
                ConverterMap converterMap = ConverterMap.builder()
                        .questionTestCaseDetail(questionTestCaseDetail)
                        .codeExecConverter(codeExecConverter)
                        .build();
                converterMapRepository.save(converterMap);
            }
        }

        return questionTestCaseResponse.getId().intValue();
    }

    @Transactional
    public Integer converterInput(ConverterInputRequest request) {
        Language languageEntity = languageRepository.findByType(request.getLanguageType()).orElseThrow();

        CodeExecConverter codeExecConverter = CodeExecConverter.builder()
                .language(languageEntity)
                .content(request.getCodeExecConverterContent())
                .returnType(request.getResultType())
                .methodName(request.getMethodName())
                .build();

        CodeExecConverter response = codeExecConverterRepository.save(codeExecConverter);
        return response.getId().intValue();
    }

    @Transactional
    public Integer questionCodeInput(QuestionCodeInputRequest request) {
        Question question = questionRepository.findById((long)request.getQuestionId()).orElseThrow();
        Language language = languageRepository.findByType(request.getLanguageType()).orElseThrow();

        QuestionLanguage questionLanguage = QuestionLanguage.builder()
                .question(question)
                .language(language)
                .initContent(request.getQuestionInitContent())
                .checkContent(request.getAnswerCheckContent())
                .build();

        QuestionLanguage response = questionLanguageRepository.save(questionLanguage);
        return response.getId().intValue();
    }

}
