package com.codementor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GenericSender {

    private RestTemplate restTemplate;

    public <T, R>ResponseEntity<R> sendData(String url, T data, Class<R> responseType) {
        return restTemplate.postForEntity(url, data, responseType);
    }
    /**
     * Example usage:
     *     public ResponseEntity<SomeResponseType> sendDataToServer() {
     *         SendDTO sendDTO = new SendDTO();
     *         // Populate sendDTO with data...
     *
     *         String serverUrl = "http://destination-server.com/api";
     *
     *         // Sending SendDTO object and expecting a response of type ReceiveDTO Type
     *         ResponseEntity<receiveDTO> response = genericSender.sendData(serverUrl, testDTO, receiveDTO.class);
     *
     *         // Handle the response...
     *         return response;
     *     }
     */
}
