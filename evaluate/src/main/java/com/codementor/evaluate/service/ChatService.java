package com.codementor.evaluate.service;

import com.codementor.evaluate.dto.EvalQuestionTestCaseDetailAndConverterDto;
import com.codementor.evaluate.dto.EvalQuestionTestCaseDto;
import com.codementor.evaluate.dto.MessageDTO;
import com.codementor.evaluate.dto.request.ChatGptRequestDTO;
import com.codementor.evaluate.dto.EvaluationDto;
import com.codementor.evaluate.dto.response.ChatGptResponseDTO;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    @Value("${openai.model}")
    private String gptModel;
    @Value("${openai.key}")
    private String apiKey;
    @Value("${openai.maxTokens}")
    private int maxTokens;
    @Value("${openai.temperature}")
    private double temperature;
    @Value("${openai.chat.url}")
    private String chatGptUrl;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();


    /**
     *
     * @param evaluationDto : kafkaListener에서 받은 evaluationRequestDto
     * @return gpt의 평가 결과
     * @throws Exception
     */
    public String gptUserCodeEvaluation(EvaluationDto evaluationDto) throws Exception {
        String gptPrompt = "";

        gptPrompt += "당신은 알고리즘 문제와 사용자의 코드를 평가하는 전문가입니다. 다음 단계에 따라 평가를 진행해주세요:\n";
        gptPrompt += "1. '문제 제목', '문제 설명', '제한 사항', '카테고리', '테스트 케이스' 를 분석합니다.\n";
        gptPrompt += "2. 사용자의 코드와 프로그래밍 언어를 분석합니다.\n";
        gptPrompt += "3. 사용자의 코드에 컴파일 에러 또는 런타임 에러가 있는지 확인합니다.\n";
        gptPrompt += "4. 코드가 문제를 해결하는지 검토합니다.\n";
        gptPrompt += "   a. 문제를 해결할 경우, 시간 복잡도를 점검하고, 가능하다면 코드를 리팩토링합니다.\n";
        gptPrompt += "   b. 문제를 해결하지 못할 경우, 적절한 모범 답안을 제공합니다.\n";
        gptPrompt += "5. 개선된 코드 또는 새로운 답안을 바탕으로 다른 버전의 코드를 생성합니다.\n";
        gptPrompt += "6. 두 가지 버전의 코드를 평가하여 최적의 솔루션을 선택합니다.\n";
        gptPrompt += "7. 최종 선택된 코드의 시간 복잡도와 효율성을 계산합니다.\n";
        gptPrompt += "8. 최종 결과가 최적의 해결책인지 세번 확인합니다.\n";
        gptPrompt += "9. 정해진 형식에 따라 최종 결과를 반환합니다.\n";

        gptPrompt += "\n\n";

        gptPrompt += "'문제 제목': \n";
        gptPrompt += evaluationDto.getQuestionTitle() + "\n";
        gptPrompt += "'문제 내용': \n";
        gptPrompt += evaluationDto.getQuestionContent() + "\n";
        gptPrompt += "'제한사항': \n";
        for (String constraint : evaluationDto.getQuestionConstraints()) {
            gptPrompt += constraint + "\n";
        }
        gptPrompt += "'카테고리': \n";
        gptPrompt += evaluationDto.getQuestionCategory() + "\n";

        gptPrompt += "\n\n";

        gptPrompt += "'테스트 케이스': \n";

        List<EvalQuestionTestCaseDto> questionTestCaseDtoList = evaluationDto.getTestCaseDtoList();
        for (int i = 0; i < questionTestCaseDtoList.size(); i++) {
            gptPrompt += "테스트 케이스 " + (i + 1) + ": \n";

            List<EvalQuestionTestCaseDetailAndConverterDto> tcAndcDtoList = questionTestCaseDtoList.get(i).getEvalTestCaseDetailAndConverterDtos();
            for (EvalQuestionTestCaseDetailAndConverterDto tcAndcDto : tcAndcDtoList) {
                gptPrompt += tcAndcDto.getTestCaseKey() + "=" + tcAndcDto.getTestCaseValue() + "\n";
            }
        }

        gptPrompt += "\n\n";

        gptPrompt += "사용자의 프로그래밍 언어: \n";
        gptPrompt += evaluationDto.getUserLanguage() + "\n";
        gptPrompt += "사용자의 코드: \n";
        gptPrompt += evaluationDto.getUserCode() + "\n";

        gptPrompt += "\n\n";

        gptPrompt += "결과 반환 형식: \n";
        gptPrompt += "\n";
        gptPrompt += "1. 컴파일 에러/런타임 에러: \n";
        gptPrompt += "'사용자의 코드'의 x번 줄에 대해 설명합니다. \n";
        gptPrompt += "예를 들어, '에러가 없습니다.', 'x번줄의 list 변수를 찾을 수가 없습니다.' 등)\n";
        gptPrompt += "2. 사용자의 코드 피드백: \n";
        gptPrompt += "코드의 특정 부분에 대한 개선 방안과 그 이유를 설명합니다.\n";
        gptPrompt += "예를 들어, '사용자의 코드'의 함수나 루프 구조에 대한 최적화 방법과 그 효율성에 대해 설명합니다.\n";
        gptPrompt += "3. 모범 답안: \n";
        gptPrompt += "최적의 해결책을 작성합니다.\n";
        gptPrompt += "4. 최종 결과: \n";
        gptPrompt += "'사용자의 코드'와 비교해서 어떤점이 개선되었는지 설명합니다.\n";

        return davinciResponse(gptPrompt);
    }

    /**
     * 이 메소드는 OpenAI API에 메시지를 전송하고 gpt 응답(응답만)을 받는 데 사용
     * @param message 는 OpenAI API에 보낼 메시지
     * @return String 은 OpenAI API로부터의 응답
     * @throws Exception 요청이 실패할 경우 예외가 발생
     */

    //Todo: Gpt Response에서 meesage.content만 받기
    public String davinciResponse(String message) throws Exception {
        System.out.println("davinci-003 called");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        MessageDTO messageDTO = new MessageDTO("user", message);
        ChatGptRequestDTO chatGptRequestDTO = new ChatGptRequestDTO(gptModel, temperature, Collections.singletonList(messageDTO));

        try {
            String payload = objectMapper.writeValueAsString(chatGptRequestDTO);
            HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(chatGptUrl, requestEntity, String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                // Directly return the response body as a string
                String responseBody = responseEntity.getBody();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);

                JsonNode choicesNode = rootNode.get("choices");
                if (!choicesNode.isMissingNode() && choicesNode.isArray() && choicesNode.has(0)) {
                    JsonNode contentNode = choicesNode.get(0).path("message").path("content");
                    if (!contentNode.isMissingNode()) {
                        return contentNode.asText();
                    }
                }
            }
        } catch (Exception e) {
            // Handle specific exceptions
            e.printStackTrace();
        }
        return null; // or some default value or throw a custom exception
    }
}
