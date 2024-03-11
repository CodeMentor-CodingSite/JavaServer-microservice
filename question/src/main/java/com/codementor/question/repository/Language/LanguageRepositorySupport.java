package com.codementor.question.repository.Language;


import com.codementor.question.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepositorySupport extends JpaRepository<Language, Long> {
    Optional<Language> findByType(String type);
}
