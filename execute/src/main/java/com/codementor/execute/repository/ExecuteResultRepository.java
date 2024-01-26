package com.codementor.execute.repository;

import com.codementor.execute.entity.ExecuteResult;
import com.codementor.execute.entity.ExecuteUsercode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExecuteResultRepository extends JpaRepository<ExecuteResult, Long> {

}
