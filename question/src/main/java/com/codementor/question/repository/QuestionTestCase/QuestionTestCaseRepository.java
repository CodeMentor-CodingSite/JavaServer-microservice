package com.codementor.question.repository.QuestionTestCase;


import com.codementor.question.entity.Question;
import com.codementor.question.entity.QuestionTestCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionTestCaseRepository extends JpaRepository<QuestionTestCase, Long> {
}
