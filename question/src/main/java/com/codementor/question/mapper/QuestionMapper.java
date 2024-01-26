package com.codementor.question.mapper;

import com.codementor.question.dto.response.QuestionDetailDtoResponse;
import com.codementor.question.dto.QuestionTestCaseDetailDto;
import com.codementor.question.dto.QuestionTestCaseDto;
import com.codementor.question.entity.*;

import java.util.stream.Collectors;

public class QuestionMapper {

    public static QuestionDetailDtoResponse toDto(Question question) {
        if (question == null) {
            return null;
        }

        return QuestionDetailDtoResponse.builder()
                .questionId(question.getId())
                .questionTitle(question.getTitle())
                .questionContent(question.getContent())
                .questionCategory(question.getCategory())
                .questionDifficulty(question.getDifficulty().name())
                .likes(question.getLikes())
                .dislikes(question.getDislikes())
                .usersAttempts(question.getAttempted())
                .usersSolved(question.getSolved())
                .questionTestCaseDtoList(question.getQuestionTestCases().stream()
                        .map(QuestionMapper::toTestCaseDto)
                        .collect(Collectors.toList()))
                .constraints(question.getQuestionConstraints().stream()
                        .map(QuestionConstraint::getContent)
                        .collect(Collectors.toList()))
                .languages(question.getQuestionLanguages().stream()
                        .map(q1 -> q1.getLanguage().getType())
                        .collect(Collectors.toList()))
                .build();
    }

    private static QuestionTestCaseDto toTestCaseDto(QuestionTestCase testCase) {
        return QuestionTestCaseDto.builder()
                .id(testCase.getId())
                .isExample(testCase.getIsExample())
                .explanation(testCase.getExplanation())
                .questionTestCaseDetailDtoList(testCase.getQuestionTestCaseDetails().stream()
                        .map(QuestionMapper::toTestCaseDetailDto)
                        .collect(Collectors.toList()))
                .build();
    }

    private static QuestionTestCaseDetailDto toTestCaseDetailDto(QuestionTestCaseDetail testCaseDetail) {
        return QuestionTestCaseDetailDto.builder()
                .id(testCaseDetail.getId())
                .key(testCaseDetail.getKey())
                .value(testCaseDetail.getValue())
                .build();
    }
}
