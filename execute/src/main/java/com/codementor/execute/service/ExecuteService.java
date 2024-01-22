package com.codementor.execute.service;

import com.codementor.execute.dto.evaluation.EvalQuestionRequest;
import com.codementor.execute.dto.evaluation.EvaluationDto;
import com.codementor.execute.dto.UserSolvedRatioSubmitDto;
import com.codementor.execute.dto.UserSolvedRatioTotalDto;
import com.codementor.execute.dto.external.QuestionDifficultyCounts;
import com.codementor.execute.entity.ExecuteUsercode;
import com.codementor.execute.repository.ExecuteUsercodeRepository;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExecuteService {

    @Value("${server.question.url}")
    private String questionUrl;

    private final ExecuteUsercodeRepository executeUsercodeRepository;

    private RestTemplate restTemplate;

    /**
     * 유저가 푼 문제 수와 제출한 문제 수를 가져온다. (비율 계산)
     * @param userId 유저 아이디
     * @return 유저가 푼 문제 수와 제출한 문제 수
     */
    public UserSolvedRatioSubmitDto getUserSolvedRatioSubmit(Long userId){
        List<ExecuteUsercode> executeUsercodeList = executeUsercodeRepository.findAllByUserId(userId);
        Long userSolved = executeUsercodeList.stream().filter(ExecuteUsercode::getIsCorrect).count();
        Long userSubmitted = (long) executeUsercodeList.size();
        return new UserSolvedRatioSubmitDto(userSolved, userSubmitted);
    }

    public UserSolvedRatioTotalDto getUserSolvedRatioTotal(Long userId){
        List<ExecuteUsercode> executeUsercodeList = executeUsercodeRepository.findAllByUserId(userId);
        List<Long> correctQuestionIdList = executeUsercodeList.stream()
                .filter(ExecuteUsercode::getIsCorrect)
                .map(ExecuteUsercode::getQuestionId)
                .collect(Collectors.toList());


        // 총 문제 수를 받는다
        QuestionDifficultyCounts questionDifficultyCounts = getQuestionDifficultyCounts(questionUrl+"/api/external/getQuestionsDifficultyCounts");
        // 총 맞은 문제를 보내고, 난이도 별 맞은 문제 수를 받아온다.
        UserSolvedRatioTotalDto sendDto = new UserSolvedRatioTotalDto(userId, correctQuestionIdList);
        UserSolvedRatioTotalDto userSolvedRatioTotalDto = getSolvedQuestionDataFromQuestionServer(questionUrl+"/api/external/getUserSolvedCounts", sendDto);

        userSolvedRatioTotalDto.setEasyProblemSolvedCount(questionDifficultyCounts.getEasyProblemsCount());
        userSolvedRatioTotalDto.setMediumProblemSolvedCount(questionDifficultyCounts.getMediumProblemsCount());
        userSolvedRatioTotalDto.setHardProblemSolvedCount(questionDifficultyCounts.getHardProblemsCount());

        return userSolvedRatioTotalDto;
    }

    private QuestionDifficultyCounts getQuestionDifficultyCounts(String url) {
        ResponseEntity<QuestionDifficultyCounts> response = restTemplate.postForEntity(url, "", QuestionDifficultyCounts.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to get valid response from " + url);
        }
    }

    private UserSolvedRatioTotalDto getSolvedQuestionDataFromQuestionServer(String url, UserSolvedRatioTotalDto request) {
        ResponseEntity<UserSolvedRatioTotalDto> response = restTemplate.postForEntity(url, request, UserSolvedRatioTotalDto.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to get valid response from " + url);
        }
    }
}
