package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String code,
        String message,
        Map<String, Object> details,
        String path
) {

    public static ErrorResponse of(HttpStatus status, String code, String message, Map<String, Object> details, String path) {
        return new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                code,
                message,
                details,
                path
        );
    }

    public static ErrorResponse of(HttpStatus status, String code, String message, String path) {
        return of(status, code, message, Map.of(), path);
    }
}
