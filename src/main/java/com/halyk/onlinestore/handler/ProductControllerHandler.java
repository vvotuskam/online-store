package com.halyk.onlinestore.handler;

import com.halyk.onlinestore.controller.ProductController;
import com.halyk.onlinestore.dto.ErrorResponse;
import com.halyk.onlinestore.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice(assignableTypes = ProductController.class)
public class ProductControllerHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message("Product not found")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
