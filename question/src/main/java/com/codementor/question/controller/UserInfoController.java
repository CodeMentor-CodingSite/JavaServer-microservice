package com.codementor.question.controller;

import com.codementor.question.core.dto.ResponseDto;
import com.codementor.question.service.UserInfoService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;

    @GetMapping("/api/question/question-count-by-tag")
    public ResponseDto<Map<String, Long>> getQuestionCountByTag(@RequestHeader("id") Long userId) {
        return ResponseDto.ok(userInfoService.getQuestionInfoByQuestionIdList(userId));
    }
}
