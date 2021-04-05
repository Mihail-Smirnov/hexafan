package com.msmir.repository;

import com.msmir.entity.user.User;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    @Query(value = "SELECT u FROM User u ORDER BY u.stats.gamesWin DESC")
    List<User> findTopWinners(Pageable pageable);
    @Query(value = "SELECT u FROM User u ORDER BY u.stats.totalGameTime DESC")
    List<User> findTopGameTime(Pageable pageable);
}
