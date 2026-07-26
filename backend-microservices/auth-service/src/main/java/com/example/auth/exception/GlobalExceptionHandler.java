package com.example.auth.exception;

import com.example.auth.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApi(ApiException ex, HttpServletRequest request) {
        log.warn("API error | Path: {} | Message: {}", request.getRequestURI(), ex.getMessage());

        return error(ex.getStatus(), ex.getMessage(), request);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleValidation(Exception ex, HttpServletRequest request) {

        log.warn("Validation error | Path: {} | Message: {}", request.getRequestURI(), ex.getMessage());

        String message = ex.getMessage();

        if (ex instanceof MethodArgumentNotValidException manv) {
            message = manv.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
        }

        return error(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {

        log.error("Unexpected error | Path: {} | Message: {}", request.getRequestURI(), ex.getMessage(), ex);

        return error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error", request);
    }

    private ResponseEntity<ErrorResponse> error(HttpStatus status, String message, HttpServletRequest request) {

        return ResponseEntity.status(status).body(new ErrorResponse(Instant.now(), MDC.get("requestId"), status.value(), status.getReasonPhrase(), message, request.getRequestURI()));
    }
}