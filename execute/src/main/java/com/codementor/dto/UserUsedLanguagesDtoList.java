package com.codementor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUsedLanguagesDtoList {
    private List<UserUsedLanguagesDto> userUsedLanguagesDtoList;
}
