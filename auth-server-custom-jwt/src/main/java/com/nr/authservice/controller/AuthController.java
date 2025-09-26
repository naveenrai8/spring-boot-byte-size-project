package com.nr.authservice.controller;

import com.nr.authservice.dto.LoginRequest;
import com.nr.authservice.dto.LoginResponse;
import com.nr.authservice.exception.EmailAlreadyExistsException;
import com.nr.authservice.exception.InvalidUserPasswordException;
import com.nr.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(this.authService.validateToken(token));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> registerUser(@RequestBody LoginRequest loginRequest) throws EmailAlreadyExistsException {
        return ResponseEntity.ok(authService.registerUser(loginRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) throws InvalidUserPasswordException {
        return ResponseEntity.ok(authService.loginUser(loginRequest));
    }
}
