package com.codementor.repository.ExecuteUsercode;

import com.codementor.entity.ExecuteUsercode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExecuteUsercodeRepositorySupport extends JpaRepository<ExecuteUsercode, Long> {

    List<ExecuteUsercode> findAllByUserId(Long userId);

    ExecuteUsercode findByIdAndUserId(Long id, Long userId);

    List<ExecuteUsercode> findAllByUserIdAndQuestionId(Long userId, Long questionId);

    List<ExecuteUsercode> findAllByUserIdAndIsCorrect(Long userId, Boolean isCorrect);

    List<ExecuteUsercode> findAllByQuestionId(Long questionId);
}
