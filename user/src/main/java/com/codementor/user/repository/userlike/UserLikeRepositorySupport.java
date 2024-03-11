package com.codementor.user.repository.userlike;

import com.codementor.user.entity.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLikeRepositorySupport extends JpaRepository<UserLike, Long> {
    boolean existsByQuestionIdAndUserId(Long questionId, Long userId);

    Optional<UserLike> findOneByQuestionIdAndUserId(Long questionId, Long userId);
}
