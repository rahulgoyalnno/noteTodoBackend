package com.smartnotes.smartnotes_backend.controller;

import com.smartnotes.smartnotes_backend.config.JwtService;
import com.smartnotes.smartnotes_backend.entity.User;
import com.smartnotes.smartnotes_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder    passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        User user = new User();
        user.setEmail(body.get("email"));
        user.setFullName(body.get("fullName"));
        user.setPassword(passwordEncoder.encode(body.get("password")));  // hash it!
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(body.get("email"), body.get("password"))
        );
        User user = userRepository.findByEmail(body.get("email")).orElseThrow();
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(Map.of("token", token));
    }
}