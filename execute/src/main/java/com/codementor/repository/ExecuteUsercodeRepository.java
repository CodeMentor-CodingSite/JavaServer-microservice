package com.codementor.repository;

import com.codementor.entity.ExecuteUsercode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExecuteUsercodeRepository extends JpaRepository<ExecuteUsercode, Long> {

    List<Long> findAllByUserIdAndIsCorrect(Long userId, Boolean isCorrect);

    List<ExecuteUsercode> findAllByUserId(Long userId);
}
