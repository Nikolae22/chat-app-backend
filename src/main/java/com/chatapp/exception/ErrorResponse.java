package com.chatapp.exception;

import java.util.Map;

public record ErrorResponse(
        int status,
        String message,
        Map<String, Object> errors
) {
}
