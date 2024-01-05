package com.codementor.question.repository;



import com.codementor.question.entity.QuestionTestCase;
import com.codementor.question.entity.QuestionTestCaseDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionTestCaseDetailRepository extends JpaRepository<QuestionTestCaseDetail, Long> {
    List<QuestionTestCaseDetail> findByQuestionTestCase(QuestionTestCase questionTestCase);
}
