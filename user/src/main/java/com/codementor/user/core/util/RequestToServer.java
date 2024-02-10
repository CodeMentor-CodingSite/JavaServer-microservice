package com.codementor.user.core.util;

import com.codementor.user.core.exception.CodeMentorException;
import com.codementor.user.core.exception.ErrorEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RequestToServer {

    @Autowired
    private RestTemplate restTemplate;

    public <T> T postDataToServer(String url, Object request, Class<T> responseType) {
        ResponseEntity<T> response = restTemplate.postForEntity(url, request, responseType);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new CodeMentorException(ErrorEnum.QUESTION_SERVER_COMMUNICATION_ERROR);
        }
    }
}
