package com.authguard.authguard_oauth2_service.Exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.authguard.authguard_oauth2_service.dtos.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({ ResourceException.class })
    public ResponseEntity<ErrorResponse> handleResourceExceptionHandler(ResourceException ex) {
        return new ResponseEntity<>(ErrorResponse.builder().message(ex
                .getMessage()).build(), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(ErrorResponse.builder().message(ex
                .getMessage()).build(), HttpStatus.BAD_GATEWAY);
    }
}
