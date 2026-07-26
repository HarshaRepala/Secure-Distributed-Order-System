package com.example.inventory.exception;

import com.example.inventory.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApi(ApiException ex, HttpServletRequest request) {

        log.warn("API error | Path: {} | Message: {}", request.getRequestURI(), ex.getMessage());

        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLock(ObjectOptimisticLockingFailureException ex, HttpServletRequest request) {

        log.error("Optimistic locking failure | Path: {} | Message: {}", request.getRequestURI(), ex.getMessage(), ex);

        return build(HttpStatus.CONFLICT, "Concurrent update detected, retry request", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOther(Exception ex, HttpServletRequest request) {

        log.error("Unexpected error | Path: {} | Message: {}", request.getRequestURI(), ex.getMessage(), ex);

        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error", request);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message, HttpServletRequest request) {

        return ResponseEntity.status(status).body(new ErrorResponse(Instant.now(), MDC.get("requestId"), status.value(), status.getReasonPhrase(), message, request.getRequestURI()));
    }
}