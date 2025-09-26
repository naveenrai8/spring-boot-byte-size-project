package com.nr.authservice.service;

import com.nr.authservice.dto.LoginRequest;
import com.nr.authservice.dto.LoginResponse;
import com.nr.authservice.exception.EmailAlreadyExistsException;
import com.nr.authservice.exception.InvalidUserPasswordException;
import com.nr.authservice.model.User;
import com.nr.authservice.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public LoginResponse registerUser(LoginRequest loginRequest) throws EmailAlreadyExistsException {
        if (this.userService.isEmailExists(loginRequest.email())) {
            throw new EmailAlreadyExistsException(String.format("Email %s already exists", loginRequest.email()));
        }
        User savedUser = this.userService.createUser(User.builder()
                .email(loginRequest.email())
                .password(passwordEncoder.encode(loginRequest.password()))
                .build());
        String token = jwtUtils.generateToken(savedUser.getEmail(), "USER");
        return LoginResponse.builder()
                .token(token)
                .build();
    }

    /*
    Before validating the token, remove the "Bearer ".
     */
    public boolean validateToken(String token) {
        try {
            this.jwtUtils.validateToken(token.substring(7));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public LoginResponse loginUser(LoginRequest loginRequest) throws InvalidUserPasswordException {
        User savedUser = this.userService.fetchUser(loginRequest.email());
        log.info("Login User: {}", savedUser);
        /*
            String comparison of saved encoded password and generated password using password Encoder is wrong
         */
        if (savedUser == null || !this.passwordEncoder.matches(loginRequest.password(), savedUser.getPassword())) {
            log.info("Saved password {}", savedUser.getPassword());
            log.info("Calculated password {}", this.passwordEncoder.encode(loginRequest.password()));

            throw new InvalidUserPasswordException("Invalid username or password");
        }
        String token = this.jwtUtils.generateToken(savedUser.getEmail(), "USER");
        return LoginResponse.builder()
                .token(token)
                .build();
    }
}
