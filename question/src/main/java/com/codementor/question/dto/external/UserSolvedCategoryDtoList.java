package com.codementor.question.dto.external;

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
