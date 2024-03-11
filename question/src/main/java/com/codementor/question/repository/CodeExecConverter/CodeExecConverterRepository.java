package com.codementor.question.repository.CodeExecConverter;


import com.codementor.question.entity.CodeExecConverter;
import com.codementor.question.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CodeExecConverterRepository extends JpaRepository<CodeExecConverter, Long> {
}
