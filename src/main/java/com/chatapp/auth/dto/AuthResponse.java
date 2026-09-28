package com.chatapp.auth.dto;

public record AuthResponse(
        String username,
        String email,
        String token
) {
}
