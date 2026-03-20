package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String code,
        String message,
        String path
) {

    public static ErrorResponse of(HttpStatus status, String code, String message, String path) {
        return new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                code,
                message,
                path
        );
    }
}
