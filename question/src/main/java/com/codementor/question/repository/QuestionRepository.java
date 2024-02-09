package com.codementor.question.repository;


import com.codementor.question.entity.Question;
import com.codementor.question.enums.QuestionDifficulty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {


    Optional<Question> findByIdAndDifficulty(Long questionId, QuestionDifficulty questionDifficulty);
}
