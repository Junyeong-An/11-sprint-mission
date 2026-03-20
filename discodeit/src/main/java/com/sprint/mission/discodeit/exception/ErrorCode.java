package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "잘못된 요청이에요."),
    LOGIN_REQUEST_REQUIRED(HttpStatus.BAD_REQUEST, "LOGIN_REQUEST_REQUIRED", "로그인 요청값이 비어있어요."),
    USER_ID_REQUIRED(HttpStatus.BAD_REQUEST, "USER_ID_REQUIRED", "유저ID가 비어있어요."),
    USERNAME_REQUIRED(HttpStatus.BAD_REQUEST, "USERNAME_REQUIRED", "유저이름이 비어있어요."),
    EMAIL_REQUIRED(HttpStatus.BAD_REQUEST, "EMAIL_REQUIRED", "이메일이 비어있어요."),
    PASSWORD_REQUIRED(HttpStatus.BAD_REQUEST, "PASSWORD_REQUIRED", "비밀번호가 비어있어요."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "해당 유저가 없어요."),
    LOGIN_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "LOGIN_USER_NOT_FOUND", "해당하는 유저가 없어요."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "DUPLICATE_USERNAME", "해당 유저이름이 이미 존재해요."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "DUPLICATE_EMAIL", "해당 이메일이 이미 존재해요."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "아이디 또는 비밀번호가 올바르지 않아요."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했어요.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
