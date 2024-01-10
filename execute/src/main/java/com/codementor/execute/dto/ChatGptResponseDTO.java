package com.codementor.execute.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.message.Message;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatGptResponseDTO {
    @JsonProperty("id")
    private String id;

    @JsonProperty("object")
    private String object;

    @JsonProperty("created")
    private long created;

    @JsonProperty("model")
    private String model;

    @JsonProperty("choices")
    private List<Choice> choices;

    @JsonProperty("usage")
    private Usage usage;

    public static class Choice {
        @JsonProperty("message")
        private Message message;

        @JsonProperty("index")
        private int index;

        @JsonProperty("logprobs")
        private Object logprobs;

        @JsonProperty("finish_reason")
        private String finishReason;

        public Message getMessage() {
            return message;
        }
    }

    // Define a nested class 'Usage' to hold the usage data
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private int promptTokens;

        @JsonProperty("completion_tokens")
        private int completionTokens;

        @JsonProperty("total_tokens")
        private int totalTokens;
    }

    public static class Message {
        @JsonProperty("role")
        private String role;

        @JsonProperty("content")
        private String content;

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }
    }
}