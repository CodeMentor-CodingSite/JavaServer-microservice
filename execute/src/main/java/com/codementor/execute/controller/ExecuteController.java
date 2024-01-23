package com.codementor.execute.controller;

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

    @GetMapping("/api/execute/problem/solved/ratio/submit")
    public UserSolvedRatioSubmitDto getUserProblemSolvedSubmitRatio(Long userId){
        return executeService.getUserSolvedRatioSubmit(userId);
    }

    @GetMapping("/api/execute/problem/solved/ratio/total")
    public UserSolvedRatioTotalDto getUserProblemSolvedTotalRatio(Long userId){
        return executeService.getUserSolvedRatioTotal(userId);
    }
}
