package com.sprint.mission.discodeit.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeitException(
            DiscodeitException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = exception.getErrorCode().getStatus();
        String code = exception.getErrorCode().getCode();
        String message = exception.getMessage();
        ErrorResponse error = ErrorResponse.of(status, code, message, exception.getDetails(), request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(
            Exception exception,
            HttpServletRequest request
    ) {
        String message = exception.getMessage();
        if (message == null || message.isBlank()) {
            message = ErrorCode.INVALID_REQUEST.getMessage();
        }
        return respond(ErrorCode.INVALID_REQUEST.getStatus(), ErrorCode.INVALID_REQUEST.getCode(), message, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception exception,
            HttpServletRequest request
    ) {
        log.error("처리되지 않은 예외 발생: {} {}", request.getMethod(), request.getRequestURI(), exception);
        return respond(
                ErrorCode.INTERNAL_SERVER_ERROR.getStatus(),
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
                request
        );
    }

    private ResponseEntity<ErrorResponse> respond(
            HttpStatus status,
            String code,
            String message,
            HttpServletRequest request
    ) {
        ErrorResponse error = ErrorResponse.of(status, code, message, request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }
}
