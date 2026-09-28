package com.chatapp.auth.dto;

import java.util.UUID;

public record UserResponse(
        UUID uuid,
        String username,
        String email
) {
}
