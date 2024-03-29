package com.codementor.controller;

import com.codementor.core.dto.ResponseDto;
import com.codementor.dto.ExecuteAllUsercodeDto;
import com.codementor.dto.ExecuteUsercodeDto;
import com.codementor.dto.UserUsedLanguagesDtoList;
import com.codementor.dto.response.UserSolvedQuestionIdAndTitleAndTimeResponse;
import com.codementor.dto.response.UserSubmitHistoryResponse;
import com.codementor.service.ExecuteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExecuteController {

    private final ExecuteService executeService;

    /**
     * 회원이 해결한 문제 (해결 / 총 제출)
     *
     * @param userId 회원 아이디
     * @return UserSolvedRatioSubmitDto
     */
    @GetMapping("/api/execute/problem/solved/ratio/submit")
    public ResponseDto getUserProblemSolvedSubmitRatio(@RequestHeader("id") Long userId) {
        return ResponseDto.ok(executeService.getUserSolvedRatioSubmit(userId));
    }

    /**
     * 회원이 해결한 문제 (해결 / 총 문제)
     *
     * @param userId 회원 아이디
     * @return UserSolvedRatioSTotalDto
     */
    @GetMapping("/api/execute/problem/solved/ratio/total")
    public ResponseDto getUserProblemSolvedTotalRatio(@RequestHeader("id") Long userId) {
        return ResponseDto.ok(executeService.getUserSolvedRatioTotal(userId));
    }

    /**
     * 회원이 해결한 문제 카테고리
     *
     * @param userId
     * @return UserSolvedCategoryDtoList
     */
    @GetMapping("/api/execute/problem/solved/category")
    public ResponseDto getUserSolvedQuestionCategory(@RequestHeader("id") Long userId) {
        return ResponseDto.ok(executeService.getUserSolvedQuestion(userId));
    }

    @GetMapping("/api/execute/problem/solved/language")
    public ResponseDto<UserUsedLanguagesDtoList> getUserUsedLanguages(@RequestHeader("id") Long userId) {
        return ResponseDto.ok(executeService.getUserUsedLanguages(userId));
    }

    @GetMapping("/api/execute/problem/solved/difficulty/counts")
    public Page<UserSolvedQuestionIdAndTitleAndTimeResponse> getUserSolvedQuestionIdAndTitleAndTime(
            @RequestHeader("id") Long userId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("difficulty") String difficulty) {
        return executeService.userSolvedQuestionIdAndTitleAndTime(userId, page, size, difficulty);
    }

    @GetMapping("/api/execute/problem/all")
    public Page<UserSubmitHistoryResponse> getUserSubmitHistory(
            @RequestHeader("id") Long userId,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        return executeService.userSubmitHistory(userId, page, size);
    }

    @GetMapping("/api/execute/{usercodeId}")
    public ResponseDto userCodeHistory(@RequestHeader("id") Long userId,
                                                           @PathVariable Long usercodeId) {
        return ResponseDto.ok(executeService.getUserSubmitHistory(userId, usercodeId));
    }

    @GetMapping("/api/execute/all/{questionId}")
    public ResponseDto<List<ExecuteUsercodeDto>> allUserCodeHistory(@RequestHeader("id") Long userId, @PathVariable Long questionId) {
        return ResponseDto.ok(executeService.allUsercodeHistory(userId, questionId));
    }

    @GetMapping("/api/execute/{questionId}/history")
    public ResponseDto<Page<ExecuteAllUsercodeDto>> getAllHistory(
            @PathVariable Long questionId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseDto.ok(executeService.getAllHistory(questionId, page, size));
    }
}
