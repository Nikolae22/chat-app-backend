package com.chatapp.exception;

import com.chatapp.auth.exceptions.InvalidCredentialsException;
import com.chatapp.auth.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(401,ex.getMessage(),null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex){
        Map<String, Object> errors=new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(),fieldError.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(400,ex.getMessage(),errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(500,ex.getMessage(),null));
    }
}
