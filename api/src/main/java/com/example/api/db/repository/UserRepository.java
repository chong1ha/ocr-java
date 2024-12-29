package com.example.api.db.repository;

import com.example.api.db.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author gunha
 * @version 1.0
 * @since 2024. 12. 29.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /** getUser */
    Optional<User> findByUsername(String username);
}
