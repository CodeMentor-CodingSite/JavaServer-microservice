package com.codementor.evaluate.dto;

import com.codementor.evaluate.dto.request.CodeExecutionConverterDto;
import com.codementor.evaluate.dto.request.QuestionTestCaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


/**
 * Kafka Listener에서 받은 메시지를 EvaluationRequestDto로 변환하기 위한 클래스
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EvaluationDto {
    /**
     * questionId: 질문 아이디
     * questionTitle: 질문 제목
     * questionContent: 질문 내용
     * questionCategory: 질문 카테고리
     * questionConstraints: 질문 제약사항
     * testCaseDetailsDtoList: 질문 테스트 케이스(List<String> keys, List<String> values)로 이루어진 dto
     */
    private Long questionId;
    private String questionTitle;
    private String questionContent;
    private String questionCategory;
    private List<String> questionConstraints;
    private List<QuestionTestCaseDto> questionTestCaseDtoList;

    /**
     * codeExecutionConverterDto: 메서드 이름, 메서드 컨텐트, 리턴 타입을 담은 dto
     * answerCheckContent: input(특정 자료구조)을 유저코드에 매개변수로 넣고 나온 결과와, answer(특정 자료구조)가 같은지 확인하기 위한 코드
     */
    private CodeExecutionConverterDto codeExecutionConverterDto;
    private String answerCheckContent;

    /**
     * userId: 유저 아이디
     * userLanguage: 유저가 선택한 언어
     * userCode: 유저가 작성한 코드
     */
    private Long userId;
    private String userLanguage;
    private String userCode;

    /**
     * testCaseResults: 테스트 케이스 결과
     * gptEvaluation: gpt 평가 결과
     */
    private List<String> testCaseResults;
    private String gptEvaluation;
}
