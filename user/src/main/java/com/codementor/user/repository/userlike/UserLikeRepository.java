package com.codementor.user.repository.userlike;

import com.codementor.user.entity.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLikeRepository extends JpaRepository<UserLike, Long> {
}
