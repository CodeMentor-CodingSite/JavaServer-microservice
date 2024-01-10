package com.codementor.execute.service;

import com.codementor.execute.dto.ChatGptRequestDTO;
import com.codementor.execute.dto.ChatGptResponseDTO;
import com.codementor.execute.dto.MessageDTO;
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
     * This method is used to send a message to the OpenAI API and receive a gpt response (only response).
     * @param message is the message to send to the OpenAI API
     * @return String is the response from the OpenAI API
     * @throws Exception if the request fails
     */
    public String davinciResponse(String message) throws Exception {
        System.out.println("davinci-003 called");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        MessageDTO messageDTO = new MessageDTO("user", message);
        ChatGptRequestDTO chatGptRequestDTO = new ChatGptRequestDTO(gptModel, temperature, Collections.singletonList(messageDTO));

        String payload = objectMapper.writeValueAsString(chatGptRequestDTO);
        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(chatGptUrl, requestEntity, String.class);
        ChatGptResponseDTO response = objectMapper.readValue(responseEntity.getBody(), ChatGptResponseDTO.class);

        String result = response.getChoices().get(0).getMessage().getContent().trim();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(result);
        String responseContent = rootNode.path("response").asText();
        return responseContent;
    }
}
