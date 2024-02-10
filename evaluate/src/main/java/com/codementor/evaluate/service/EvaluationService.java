package com.codementor.evaluate.service;

import com.codementor.evaluate.dto.EvalQuestionTestCaseDetailAndConverterDto;
import com.codementor.evaluate.dto.EvalQuestionTestCaseDto;
import com.codementor.evaluate.dto.EvaluationDto;
import com.jcraft.jsch.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 코드 실행 서버와 관련된 서비스
 */
@Service
@RequiredArgsConstructor
public class EvaluationService {

    @Value("${CodeEvaluationServer.host}")
    private String HOST;
    @Value("${CodeEvaluationServer.username}")
    private String USERNAME;
    @Value("${CodeEvaluationServer.password}")
    private String PASSWORD;
    @Value("${CodeEvaluationServer.port}")
    private int PORT;

    private static Session session;
    private static Channel channel;

    /**
     * 코드 실행 서버에 코드 실행 요청을 보내고, 결과 값을 받아옴
     *
     * @param evaluationDto 코드 실행에 필요한 정보들이 담긴 객체
     * @return
     */
    public ArrayList<String> processExecutionResults(EvaluationDto evaluationDto) {
        ArrayList<String> executionResults = new ArrayList<>(); // 코드 실행에 대한 결과 값들을 답는 배열
        ArrayList<String> executionPrompts = new ArrayList<>(); // 최종 코드 완성 배열

        // 코드 실행을 위한 커멘트 프롬프트를 생성 ( java / python 구분 )
        if (evaluationDto.getUserLanguage().equals("Python")) {
            executionPrompts = constructPythonCodeExecutionStringCommand(evaluationDto);
        } else if (evaluationDto.getUserLanguage().equals("Java")) {
            executionPrompts = constructJavaCodeExecutionStringCommand(evaluationDto);
        }

        // 각 커멘트 프롬프트를 실행하고 결과 값을 받아옴
        for (String executionPrompt : executionPrompts) {
            System.out.println(executionPrompt);
            String executionResult = sendRequestToExecuteServer(executionPrompt); //Todo: \n is not needed. use string.trim()
            System.out.println(executionResult);
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
            List<EvalQuestionTestCaseDetailAndConverterDto> questionTestCaseDetailsDtoList = questionTestCaseDto.getEvalTestCaseDetailAndConverterDtos();

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
                pythonScript += "\n";

                if (codeExecConverterContent != null) {
                    pythonScript += codeExecConverterContent;
                    pythonScript += "\n";
                    pythonScript += key + " = " + methodName + "(" + value + ")";
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

    private ArrayList<String> constructJavaCodeExecutionStringCommand(EvaluationDto evaluationDto) {
        ArrayList<String> prompts = new ArrayList<>();

        // TestCase 관련
        List<EvalQuestionTestCaseDto> questionTestCaseDtoList = evaluationDto.getTestCaseDtoList();

        for (EvalQuestionTestCaseDto questionTestCaseDto : questionTestCaseDtoList) {
            // RealMain Class
            StringBuilder finalCode = new StringBuilder("echo 'import java.util.*;").append("\n\n\n")
                    .append("public class RealMain {\n");

            // converter ( set - 중복 제거 )
            questionTestCaseDto.getEvalTestCaseDetailAndConverterDtos()
                    .stream()
                    .map(EvalQuestionTestCaseDetailAndConverterDto::getCodeExecConverterContent)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet())
                    .forEach(i -> finalCode.append(i).append("\n\n"));

            // main method
            finalCode.append(
                    "    public static void main(String[] args) throws Exception{\n" +
                            "        Solution solution = new Solution();\n" +
                            "\n");

            // parameter
            questionTestCaseDto.getEvalTestCaseDetailAndConverterDtos()
                    .forEach(
                            i -> finalCode.append("        ")
                                    .append(i.getReturnType()).append(" ")
                                    .append(i.getTestCaseKey()).append(" = ")
                                    .append(i.getMethodName()).append("(\"")
                                    .append(i.getTestCaseValue()).append("\");\n")
                    );

            // answer check
            finalCode.append("\n        ").append(evaluationDto.getAnswerCheckContent()).append("\n")
                    .append("    }").append("\n\n");

            finalCode.append("    static ").append(evaluationDto.getUserCode()).append("\n")
                    .append("}\n\n");

            // java 실행
            finalCode.append("' > RealMain.java && javac -encoding ISO-8859-1 RealMain.java && timeout -s 9 5s java -Xmx128m RealMain");

            prompts.add(finalCode.toString());
        }

        return prompts;
    }

    private String sendRequestToExecuteServer(String codeExecutionStringCommand) {
        // send request to execute server
        if (session == null || !session.isConnected()) {
            sshSessionOpen();
        }
        // Execute command
        return executeCode(codeExecutionStringCommand);
    }

    private void sshSessionOpen() {
        try {
            session = new JSch().getSession(USERNAME, HOST, PORT);
            session.setPassword(PASSWORD);

            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }

        System.out.println("New Session Open");
    }

    // ssh 세션 활용한 Exec 채널로 코드 실행 및 결과 반환
    public String executeCode(String finalCode) {
        String response = "";

        try {
            channel = session.openChannel("exec");
            ChannelExec channelExec = (ChannelExec) channel;

            String cmd = finalCode;
            channelExec.setCommand(cmd);

            StreamGobbler outputGobbler = new StreamGobbler(channelExec.getInputStream());
            StreamGobbler errorGobbler = new StreamGobbler(channelExec.getErrStream());

            new Thread(outputGobbler).start();
            new Thread(errorGobbler).start();

            channelExec.connect();

            // 출력 도착 지연 때문인지 조금 기다려야 하는 듯
            while (!outputGobbler.isDone() || !errorGobbler.isDone()) {
                Thread.sleep(100);
            }

            response = outputGobbler.getOutput();
            String errorString = errorGobbler.getOutput();

            // 에러 확인
            if (!errorString.isEmpty()) {
                return "Error: " + errorString;
            }

            // Exception 확인
        } catch (JSchException | InterruptedException | IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } finally {
            if (channel != null && channel.isConnected()) {
                channel.disconnect();
            }
        }

        return response;
    }

    // StreamGobbler class
    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private StringBuilder output = new StringBuilder();
        private volatile boolean done = false;

        StreamGobbler(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                done = true;
            }
        }

        public String getOutput() {
            return output.toString();
        }

        public boolean isDone() {
            return done;
        }
    }
}
