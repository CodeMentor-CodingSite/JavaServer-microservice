package com.codementor.service;

import com.codementor.core.util.RequestToServer;
import com.codementor.dto.*;
import com.codementor.dto.external.QuestionDifficultyCounts;
import com.codementor.dto.response.UserSolvedQuestionIdAndTitleAndTimeResponse;
import com.codementor.dto.external.UserSolvedQuestionIdList;
import com.codementor.dto.response.UserSubmitHistoryResponse;
import com.codementor.entity.ExecuteUsercode;
import com.codementor.repository.ExecuteUsercodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExecuteService {

    @Value("${server.question.url}")
    private String questionUrl;

    private final RequestToServer requestToServer;

    private final ExecuteUsercodeRepository executeUsercodeRepository;

    /**
     * 유저가 푼 문제 수와 제출한 문제 수를 가져온다.
     *
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
     *
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
     *
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

    public UserUsedLanguagesDtoList getUserUsedLanguages(Long userId) {
        List<ExecuteUsercode> executeUsercodeList = executeUsercodeRepository.findAllByUserId(userId);
        Map<String, Integer> collectedLanguages = executeUsercodeList.stream()
                .filter(ExecuteUsercode::getIsCorrect)
                .sorted(Comparator.comparing(ExecuteUsercode::getUserLanguage))
                .collect(Collectors.groupingBy(ExecuteUsercode::getUserLanguage, Collectors.summingInt(e -> 1)));

        List<UserUsedLanguagesDto> userUsedLanguagesDtoList = collectedLanguages.keySet()
                .stream()
                .map(key -> new UserUsedLanguagesDto(key, collectedLanguages.get(key)))
                .collect(Collectors.toList());

        return new UserUsedLanguagesDtoList(userUsedLanguagesDtoList);
    }

    /**
     * 유저가 푼 문제에 대한 Id, 제목, 시간을 가져와서 페이징 처리하여 반환한다.
     * @param userId 유저 아이디
     * @param page 페이지
     * @param size 사이즈
     * @param difficulty 난이도
     * @return
     */
    public Page<UserSolvedQuestionIdAndTitleAndTimeResponse> userSolvedQuestionIdAndTitleAndTime(
            Long userId, int page, int size, String difficulty) {
        List<UserSolvedQuestionIdAndTitleAndTimeResponse> responseList = new ArrayList<>();
        List<ExecuteUsercode> executeUsercodeList = executeUsercodeRepository.findAllByUserIdAndIsCorrect(userId, true);
        for (var executeUsercode : executeUsercodeList){
            responseList.add(UserSolvedQuestionIdAndTitleAndTimeResponse.of(executeUsercode, difficulty));
        }

        String questionNameFromIdUrl = questionUrl + "/api/external/getQuestionTitleAndDifficultyFromId";
        List<UserSolvedQuestionIdAndTitleAndTimeResponse> finalResponse = requestToServer.postDataToServer(
                questionNameFromIdUrl,
                responseList,
                List.class);

        Pageable pageableWithSort = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timeStamp"));
        return new PageImpl<>(finalResponse, pageableWithSort, finalResponse.size());
    }

    public Page<UserSubmitHistoryResponse> userSubmitHistory(Long userId, int page, int size) {
        List<UserSubmitHistoryResponse> responseList = new ArrayList<>();
        List<ExecuteUsercode> executeUsercodeList = executeUsercodeRepository.findAllByUserId(userId);
        for (var executeUsercode : executeUsercodeList){
            responseList.add(UserSubmitHistoryResponse.of(executeUsercode));
        }

        String helperUrl = questionUrl + "/api/external/getSubmitHistory";
        List<UserSubmitHistoryResponse> finalResponse = requestToServer.postDataToServer(
                helperUrl,
                responseList,
                List.class);

        Pageable pageableWithSort = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timeStamp"));
        return new PageImpl<>(finalResponse, pageableWithSort, finalResponse.size());
    }

    public ExecuteUsercodeDto userCodeHistory(Long userId, Long usercodeId) {
        return ExecuteUsercodeDto.from(executeUsercodeRepository.findById(usercodeId).orElseThrow());
    }

    //풀었던 문제 엔터티들의 Id를 가져온다.
    private List<Long> getSolvedExecuteUserCodeList(Long userId) {
        List<ExecuteUsercode> executeUsercodeList = executeUsercodeRepository.findAllByUserId(userId);
        Set<Long> correctQuestionIdList = executeUsercodeList.stream()
                .filter(ExecuteUsercode::getIsCorrect)
                .map(ExecuteUsercode::getQuestionId)
                .collect(Collectors.toSet());
        return new ArrayList<>(correctQuestionIdList);
    }
}
