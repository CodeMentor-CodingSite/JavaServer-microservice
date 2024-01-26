package com.codementor.execute.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSolvedCategoryDtoList {
    private List<UserSolvedCategoryDto> userSolvedCategoryDtoList;
}
