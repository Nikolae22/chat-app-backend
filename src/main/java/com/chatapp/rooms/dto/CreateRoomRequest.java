package com.chatapp.rooms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateRoomRequest(
        @NotNull(message = "Room name is Required")
        @NotEmpty(message = "Room name is Required")
        @NotBlank(message = "Room name is required")
        String name,
        @NotNull(message = "Room description is Required")
        @NotEmpty(message = "Room description is Required")
        @NotBlank(message = "Room description is required")
        @Size(max = 500,message = "Max 500 ch")
        String description,
        boolean isPrivate
) {
}
