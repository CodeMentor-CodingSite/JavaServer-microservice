package com.codementor.evaluate.dto.request;

import com.codementor.evaluate.dto.MessageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * chatGpt와의 통신에 필요한 간략한 설정들을 담은 dto
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatGptRequestDTO {
    private String model;

    private double temperature;

    private List<MessageDTO> messages;
}
