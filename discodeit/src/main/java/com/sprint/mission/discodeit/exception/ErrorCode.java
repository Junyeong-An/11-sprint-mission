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
    MESSAGE_ID_REQUIRED(HttpStatus.BAD_REQUEST, "MESSAGE_ID_REQUIRED", "메시지ID가 비어있어요."),
    MESSAGE_CONTENT_REQUIRED(HttpStatus.BAD_REQUEST, "MESSAGE_CONTENT_REQUIRED", "메시지 내용이 비어있어요."),
    CHANNEL_ID_REQUIRED(HttpStatus.BAD_REQUEST, "CHANNEL_ID_REQUIRED", "채널ID가 비어있어요."),
    BINARY_CONTENT_ID_REQUIRED(HttpStatus.BAD_REQUEST, "BINARY_CONTENT_ID_REQUIRED", "첨부파일ID가 비어있어요."),
    READ_STATUS_ID_REQUIRED(HttpStatus.BAD_REQUEST, "READ_STATUS_ID_REQUIRED", "읽음상태ID가 비어있어요."),
    LAST_READ_AT_REQUIRED(HttpStatus.BAD_REQUEST, "LAST_READ_AT_REQUIRED", "마지막 읽은 시간이 비어있어요."),
    USER_STATUS_ID_REQUIRED(HttpStatus.BAD_REQUEST, "USER_STATUS_ID_REQUIRED", "유저상태ID가 비어있어요."),
    LAST_CONNECTED_AT_REQUIRED(HttpStatus.BAD_REQUEST, "LAST_CONNECTED_AT_REQUIRED", "마지막 접속 시간이 비어있어요."),
    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "MESSAGE_NOT_FOUND", "해당 메시지가 없어요."),
    BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "BINARY_CONTENT_NOT_FOUND", "해당 첨부파일이 없어요."),
    READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "READ_STATUS_NOT_FOUND", "해당 읽음상태가 없어요."),
    USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_STATUS_NOT_FOUND", "해당 유저상태가 없어요."),
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "CHANNEL_NOT_FOUND", "해당 채널이 없어요."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "해당 유저가 없어요."),
    DUPLICATE_READ_STATUS(HttpStatus.CONFLICT, "DUPLICATE_READ_STATUS", "해당 채널/유저의 읽음상태가 이미 존재해요."),
    DUPLICATE_USER_STATUS(HttpStatus.CONFLICT, "DUPLICATE_USER_STATUS", "해당 유저의 상태가 이미 존재해요."),
    LOGIN_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "LOGIN_USER_NOT_FOUND", "해당하는 유저가 없어요."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "DUPLICATE_USERNAME", "해당 유저이름이 이미 존재해요."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "DUPLICATE_EMAIL", "해당 이메일이 이미 존재해요."),
    PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED", "비공개 채널은 수정할 수 없어요."),
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
