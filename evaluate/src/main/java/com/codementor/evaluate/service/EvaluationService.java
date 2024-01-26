package com.codementor.evaluate.service;

import com.codementor.evaluate.dto.EvalQuestionTestCaseDetailAndConverterDto;
import com.codementor.evaluate.dto.EvalQuestionTestCaseDto;
import com.codementor.evaluate.dto.request.CodeExecutionConverterDto;
import com.codementor.evaluate.dto.EvaluationDto;
import com.codementor.evaluate.dto.request.QuestionTestCaseDetailsDto;
import com.codementor.evaluate.dto.request.QuestionTestCaseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleBiFunction;

/**
 * 코드 실행 서버와 관련된 서비스
 */
@Service
@RequiredArgsConstructor
public class EvaluationService {


    /** 코드 실행 서버에 코드 실행 요청을 보내고, 결과 값을 받아옴
     * @param evaluationDto 코드 실행에 필요한 정보들이 담긴 객체
     * @return
     */
    public ArrayList<String> processExecutionResults(EvaluationDto evaluationDto){
        // 코드 실행에 대한 결과 값들을 답는 배열
        ArrayList<String> executionResults = new ArrayList<>();

        // 코드 실행을 위한 커멘트 프롬프트를 생성
        ArrayList<String> executionPrompts = constructPythonCodeExecutionStringCommand(evaluationDto);

        // 각 커멘트 프롬프트를 실행하고 결과 값을 받아옴
        for (String executionPrompt : executionPrompts) {
            String executionResult = sendRequestToExecuteServer(executionPrompt);
            executionResults.add(executionResult);
        }

        return executionResults;
    }

    private ArrayList<String> constructPythonCodeExecutionStringCommand(EvaluationDto evaluationDto) {
        ArrayList<String> prompts = new ArrayList<>();

        // UserCode 관련
        String userCode = evaluationDto.getUserCode();


        // TestCase 관련
        List<EvalQuestionTestCaseDto> questionTestCaseDtoList = evaluationDto.getTestCaseDtoList();

        // Answer Check 관련
        String answerCheckContent = evaluationDto.getAnswerCheckContent();


        for (EvalQuestionTestCaseDto questionTestCaseDto : questionTestCaseDtoList) {
            String pythonScript = "";
            List<EvalQuestionTestCaseDetailAndConverterDto> questionTestCaseDetailsDtoList = questionTestCaseDto.getEvalQuestionTestCaseDetailAndConverterDtos();

            // 유저 코드 입력
            pythonScript += userCode;

            pythonScript += "\n\n\n";

            // 테스트케이스 입력 및 변환
            for (EvalQuestionTestCaseDetailAndConverterDto tcAndcDto : questionTestCaseDetailsDtoList) {
                String key = tcAndcDto.getTestCaseKey();
                String value = tcAndcDto.getTestCaseValue();
                String codeExecConverterContent = tcAndcDto.getCodeExecConverterContent();
                String returnType = tcAndcDto.getReturnType();
                String methodName = tcAndcDto.getMethodName();

                pythonScript += key + " = " + value;

                if (codeExecConverterContent != null) {
                    pythonScript += codeExecConverterContent;
                    pythonScript += "\n";
                    pythonScript += key + " = " + methodName + "(" + value + ")";
                } else {
                    pythonScript += key + " = " + value;
                }

                pythonScript += "\n";
            }

            pythonScript += "\n\n\n";

            pythonScript += answerCheckContent;

            String command = "python3 -c \"" + pythonScript + "\"";
            prompts.add(command);
        }

        return prompts;
    }

    // Todo : 코드 실행 서버와 연결하고, 결과값 받아오기
    private String sendRequestToExecuteServer(String codeExecutionStringCommand) {

        // send request to execute server
        String result = "";
        // Convert to proper string result
        return result;
    }

}
