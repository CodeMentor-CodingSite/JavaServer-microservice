package com.codementor.execute.controller;

import com.codementor.execute.dto.UserSolvedCategoryDtoList;
import com.codementor.execute.dto.UserSolvedRatioSubmitDto;
import com.codementor.execute.dto.UserSolvedRatioTotalDto;
import com.codementor.execute.service.ExecuteService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExecuteController {

    private final ExecuteService executeService;

    /**
     * 회원이 해결한 문제 비율 (해결 / 총 제출)
     * @param userId 회원 아이디
     * @return UserSolvedRatioSubmitDto
     */
    @GetMapping("/api/execute/problem/solved/ratio/submit")
    public UserSolvedRatioSubmitDto getUserProblemSolvedSubmitRatio(Long userId){
        return executeService.getUserSolvedRatioSubmit(userId);
    }

    /**
     * 회원이 해결한 문제 비율 (해결 / 총 문제)
     * @param userId 회원 아이디
     * @return UserSolvedRatioSTotalDto
     */
    @GetMapping("/api/execute/problem/solved/ratio/total")
    public UserSolvedRatioTotalDto getUserProblemSolvedTotalRatio(Long userId){
        return executeService.getUserSolvedRatioTotal(userId);
    }

    @GetMapping("/api/execute/problem/solved/category")
    public UserSolvedCategoryDtoList getUserSolvedQuestionCategory(Long userId){
        return executeService.getUserSolvedQuestion(userId);
    }

}
