package com.chatapp.messages.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        UUID roomId,
        UUID userId,
        String username,
        String content,
        LocalDateTime sendAt
) {
}
