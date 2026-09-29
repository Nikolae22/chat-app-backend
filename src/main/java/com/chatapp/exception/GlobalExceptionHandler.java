package com.chatapp.exception;

import com.chatapp.auth.exceptions.InvalidCredentialsException;
import com.chatapp.auth.exceptions.UserAlreadyExistsException;
import com.chatapp.rooms.exceptions.RoomAlreadyExistsException;
import com.chatapp.rooms.exceptions.RoomNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(
            UserAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(409,ex.getMessage(),null));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(
            InvalidCredentialsException ex){
        Map<String,String> errors = new HashMap<>();
        errors.put("credentials", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(401,ex.getMessage(),errors));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex){
        Map<String, String> errors=new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(),fieldError.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400,ex.getMessage(),errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(500,ex.getMessage(),null));
    }

    @ExceptionHandler(RoomAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleRoomAlreadyExistsException(RoomAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        409,ex.getMessage(),null
                ));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoomNotFoundException(RoomNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        404,ex.getMessage(),null
                ));
    }
}
