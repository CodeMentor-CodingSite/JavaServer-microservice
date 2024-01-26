package com.codementor.execute.repository;

import com.codementor.execute.entity.ExecuteUsercode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExecuteUsercodeRepository extends JpaRepository<ExecuteUsercode, Long> {

    List<Long> findAllByUserIdAAndIsCorrect(Long userId, Boolean isCorrect);

    List<ExecuteUsercode> findAllByUserId(Long userId);
}
