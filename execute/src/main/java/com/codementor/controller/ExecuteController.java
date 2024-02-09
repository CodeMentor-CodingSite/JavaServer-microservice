package com.codementor.controller;

import com.codementor.core.dto.ResponseDto;
import com.codementor.dto.UserUsedLanguagesDtoList;
import com.codementor.dto.response.UserSolvedQuestionIdAndTitleAndTimeResponse;
import com.codementor.service.ExecuteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExecuteController {

    private final ExecuteService executeService;

    /**
     * 회원이 해결한 문제 (해결 / 총 제출)
     * @param userId 회원 아이디
     * @return UserSolvedRatioSubmitDto
     */
    @GetMapping("/api/execute/problem/solved/ratio/submit")
    public ResponseDto getUserProblemSolvedSubmitRatio(@RequestHeader("userId") Long userId){
        return ResponseDto.ok(executeService.getUserSolvedRatioSubmit(userId));
    }

    /**
     * 회원이 해결한 문제 (해결 / 총 문제)
     * @param userId 회원 아이디
     * @return UserSolvedRatioSTotalDto
     */
    @GetMapping("/api/execute/problem/solved/ratio/total")
    public ResponseDto getUserProblemSolvedTotalRatio(@RequestHeader("userId") Long userId){
        return ResponseDto.ok(executeService.getUserSolvedRatioTotal(userId));
    }

    /**
     * 회원이 해결한 문제 카테고리
     * @param userId
     * @return UserSolvedCategoryDtoList
     */
    @GetMapping("/api/execute/problem/solved/category")
    public ResponseDto getUserSolvedQuestionCategory(@RequestHeader("userId") Long userId){
        return ResponseDto.ok(executeService.getUserSolvedQuestion(userId));
    }

    @GetMapping("/api/execute/problem/solved/language")
    public ResponseDto<UserUsedLanguagesDtoList> getUserUsedLanguages(@RequestHeader("userId") Long userId) {
        return ResponseDto.ok(executeService.getUserUsedLanguages(userId));
    }

    @GetMapping("/api/execute/problem/solved/difficulty/counts")
    public Page<UserSolvedQuestionIdAndTitleAndTimeResponse> getUserSolvedQuestionIdAndTitleAndTime(
            @RequestHeader("userId") Long userId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("difficulty") String difficulty) {
        return executeService.userSolvedQuestionIdAndTitleAndTime(userId, page, size, difficulty);
    }
}
