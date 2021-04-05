package com.msmir.repository;

import com.msmir.entity.GameReplay;
import com.msmir.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameReplayRepository extends JpaRepository<GameReplay, Long> {
  @Query(value = "SELECT r FROM GameReplay r WHERE r.whitePlayer = :user OR"
      + " r.blackPlayer = :user ORDER BY r.endDate DESC")
  Page<GameReplay> findReplays(@Param("user") User user, Pageable pageable);
}
