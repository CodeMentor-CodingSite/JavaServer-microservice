package com.codementor.question.repository;



import com.codementor.question.entity.Language;
import com.codementor.question.entity.Question;
import com.codementor.question.entity.QuestionLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionLanguageRepository extends JpaRepository<QuestionLanguage, Long> {
    Optional<QuestionLanguage> findByQuestionAndLanguage(Question question, Language language);
}
