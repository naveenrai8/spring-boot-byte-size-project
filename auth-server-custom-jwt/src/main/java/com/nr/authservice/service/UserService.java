package com.nr.authservice.service;

import com.nr.authservice.model.User;
import com.nr.authservice.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public boolean isEmailExists(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public User createUser(User user) {
        return this.userRepository.save(user);
    }

    public User fetchUser(@NotBlank(message = "email can't be blank") String email) {
        return this.userRepository.findByEmail(email);
    }
}
