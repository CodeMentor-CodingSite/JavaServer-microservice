package com.codementor.question.repository.ConverterMap;


import com.codementor.question.entity.ConverterMap;
import com.codementor.question.entity.QuestionTestCaseDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConverterMapRepository extends JpaRepository<ConverterMap, Long> {
}
