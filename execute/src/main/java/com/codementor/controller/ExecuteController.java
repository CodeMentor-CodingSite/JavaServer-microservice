package com.codementor.controller;

import com.codementor.core.dto.ResponseDto;
import com.codementor.dto.ExecuteUsercodeDto;
import com.codementor.dto.UserUsedLanguagesDtoList;
import com.codementor.dto.response.UserSolvedQuestionIdAndTitleAndTimeResponse;
import com.codementor.dto.response.UserSubmitHistoryResponse;
import com.codementor.service.ExecuteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/api/execute/problem/all")
    public Page<UserSubmitHistoryResponse> getUserSubmitHistory(
            @RequestHeader("userId") Long userId,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        return executeService.userSubmitHistory(userId, page, size);
    }

    @GetMapping("/api/execute/{usercodeId}")
    public ResponseDto<ExecuteUsercodeDto> userCodeHistory(@RequestHeader("userId") Long userId, @PathVariable Long usercodeId) {
        return ResponseDto.ok(executeService.userCodeHistory(userId, usercodeId));
    }
}
