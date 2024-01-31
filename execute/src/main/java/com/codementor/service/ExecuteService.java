package com.codementor.service;

import com.codementor.core.util.RequestToServer;
import com.codementor.dto.UserSolvedCategoryDtoList;
import com.codementor.dto.UserSolvedRatioSubmitDto;
import com.codementor.dto.UserSolvedRatioTotalDto;
import com.codementor.dto.external.QuestionDifficultyCounts;
import com.codementor.dto.external.UserSolvedQuestionIdList;
import com.codementor.entity.ExecuteUsercode;
import com.codementor.repository.ExecuteUsercodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExecuteService {

    @Value("${server.question.url}")
    private String questionUrl;

    private final RequestToServer requestToServer;

    private final ExecuteUsercodeRepository executeUsercodeRepository;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 유저가 푼 문제 수와 제출한 문제 수를 가져온다.
     * @param userId 유저 아이디
     * @return 유저가 푼 문제 수와 제출한 문제 수
     */
    public UserSolvedRatioSubmitDto getUserSolvedRatioSubmit(Long userId) {
        List<ExecuteUsercode> executeUsercodeList = executeUsercodeRepository.findAllByUserId(userId);

        Long userSolved = executeUsercodeList.stream().filter(ExecuteUsercode::getIsCorrect).count();
        Long userSubmitted = (long) executeUsercodeList.size();

        return UserSolvedRatioSubmitDto.builder()
                .userProblemSolvedCount(userSolved)
                .userProblemSubmittedCount(userSubmitted)
                .build();
    }

    /**
     * 유저가 푼 문제 수와 전체 문제 수를 가져온다.
     * @param userId 유저 아이디
     * @return 유저가 푼 문제 수와 전체 문제 수
     * 1. 풀었던 문제 엔터티 리스트를 가져온다.
     * 2. 총 맞은 문제들을 보내고, 맞은 문제들을 난이도별로 분류하여 카운트한 Dto를 받아온다.
     * 3. 총 문제 수를 받는다
     * 4. 총 풀었던 문제 수를 업데이트한다.
     */
    public UserSolvedRatioTotalDto getUserSolvedRatioTotal(Long userId) {
        List<Long> correctQuestionIdList = getSolvedExecuteUserCodeList(userId); // 1.

        UserSolvedRatioTotalDto sendDto = UserSolvedRatioTotalDto.builder() // 2.
                .userId(userId)
                .questionIdList(correctQuestionIdList)
                .build();
        String userSolvedCountsUrl = questionUrl + "/api/external/getUserSolvedCounts";
        UserSolvedRatioTotalDto userSolvedRatioTotalDto = requestToServer.postDataToServer(userSolvedCountsUrl, sendDto, UserSolvedRatioTotalDto.class);

        String questionDifficultyCountsUrl = questionUrl + "/api/external/getQuestionsDifficultyCounts"; // 3.
        QuestionDifficultyCounts questionDifficultyCounts = requestToServer.postDataToServer(questionDifficultyCountsUrl, null, QuestionDifficultyCounts.class);

        userSolvedRatioTotalDto.updateProblemCountWith(questionDifficultyCounts); // 4.

        return userSolvedRatioTotalDto;
    }

    /**
     * 유저가 푼 문제에 대한 카테고리와 난이도가 포함된 정보를 가져온다.
     * @param userId 유저 아이디
     * @return 유저가 푼 문제에 대한 카테고리와 난이도가 포함된 정보
     * 1. 풀었던 문제 엔터티 리스트를 가져온다.
     * 2. 유저 아이디를 보내고, 문제 Id, 카테고리, 난이도가 포함된 Dto List를 받아온다.
     */
    public UserSolvedCategoryDtoList getUserSolvedQuestion(Long userId) {
        List<Long> correctQuestionIdList = getSolvedExecuteUserCodeList(userId); // 1.

        UserSolvedQuestionIdList sendDto = UserSolvedQuestionIdList.builder() // 2.
                .userId(userId)
                .problemIdList(correctQuestionIdList)
                .build();
        String userSolvedCategoryUrl = questionUrl + "/api/external/getUserSolvedCategory";
        return requestToServer.postDataToServer(userSolvedCategoryUrl, sendDto, UserSolvedCategoryDtoList.class);
    }

    //풀었던 문제 엔터티들의 Id를 가져온다.
    private List<Long> getSolvedExecuteUserCodeList(Long userId) {
        List<ExecuteUsercode> executeUsercodeList = executeUsercodeRepository.findAllByUserId(userId);
        List<Long> correctQuestionIdList = executeUsercodeList.stream()
                .filter(ExecuteUsercode::getIsCorrect)
                .map(ExecuteUsercode::getQuestionId)
                .collect(Collectors.toList());
        return correctQuestionIdList;
    }
}
