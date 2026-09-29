package com.chatapp.rooms.exceptions;

public class RoomAlreadyExistsException extends RuntimeException {
    public RoomAlreadyExistsException(String message) {
        super(message);
    }
}
