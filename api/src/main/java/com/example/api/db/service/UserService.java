package com.example.api.db.service;

import com.example.api.db.domain.User;
import com.example.api.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author gunha
 * @version 1.0
 * @since 2024. 12. 29.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Username으로 UserId 조회
     *
     * @param username 유저네임
     * @throws RuntimeException User 없으면 예외
     * @return userId
     */
    public Long getUserIdByUsername(String username) {

        // 조회
        Optional<User> userOptional = userRepository.findByUsername(username);

        return userOptional.map(User::getUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
