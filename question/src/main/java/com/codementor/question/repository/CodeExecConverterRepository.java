package com.codementor.question.repository;


import com.codementor.question.entity.CodeExecConverter;
import com.codementor.question.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeExecConverterRepository extends JpaRepository<CodeExecConverter, Long> {
    Optional<CodeExecConverter> findAllByIdAndLanguage(Long Id, Language language);
}
