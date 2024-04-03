package com.halyk.onlinestore.handler;

import com.halyk.onlinestore.controller.ProductController;
import com.halyk.onlinestore.dto.ErrorResponse;
import com.halyk.onlinestore.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice(assignableTypes = ProductController.class)
public class ProductControllerHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        log.warn("Not found exception occurred", e);
        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        log.warn("Validation exception occurred", e);
        var errorFields = e.getBindingResult().getAllErrors()
                .stream()
                .map(error -> {
                    String field = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();
                    return ErrorResponse.ErrorField.builder()
                            .field(field)
                            .message(message)
                            .build();
                })
                .toList();

        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Bad request")
                .timestamp(LocalDateTime.now())
                .fieldErrors(errorFields)
                .build();

        return ResponseEntity.badRequest().body(response);
    }
}
