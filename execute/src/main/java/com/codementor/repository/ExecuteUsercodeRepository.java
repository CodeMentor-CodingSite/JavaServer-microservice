package com.codementor.repository;

import com.codementor.entity.ExecuteUsercode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExecuteUsercodeRepository extends JpaRepository<ExecuteUsercode, Long> {

    List<ExecuteUsercode> findAllByUserId(Long userId);

    ExecuteUsercode findByIdAndUsercodeId(Long id, Long usercodeId);

    List<ExecuteUsercode> findAllByUserIdAndQuestionId(Long userId, Long questionId);

    List<ExecuteUsercode> findAllByUserIdAndIsCorrect(Long userId, Boolean isCorrect);
}
