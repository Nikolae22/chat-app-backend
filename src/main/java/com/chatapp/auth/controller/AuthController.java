package com.chatapp.auth.controller;

import com.chatapp.auth.AuthService;
import com.chatapp.auth.dto.AuthResponse;
import com.chatapp.auth.dto.LoginRequest;
import com.chatapp.auth.dto.RegisterRequest;
import com.chatapp.auth.dto.UserResponse;
import com.chatapp.auth.users.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(request));
    }


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(
            @AuthenticationPrincipal User user){
     return ResponseEntity.ok(new UserResponse(
             user.getId(), user.getUsername(), user.getEmail()
     ));
    }
}
