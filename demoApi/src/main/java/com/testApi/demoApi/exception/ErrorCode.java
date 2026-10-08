package com.testApi.demoApi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    VIDEO_EXISTED(1001, "User existed", HttpStatus.BAD_REQUEST),
    VIDEO_NOT_FOUND(1002, "Video not found", HttpStatus.NOT_FOUND),
    UNKNOWN_ERROR(999, "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR),
    TITLE_IS_NOT_VALID_1(2001, "Title is not valid", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1004, "Invalid message key", HttpStatus.BAD_REQUEST),
    DESCRIPTION_IS_NOT_VALID_1(2002, "Description is not valid", HttpStatus.BAD_REQUEST),
    VIDEO_URL_IS_NOT_VALID_1(2003, "Video is not valid", HttpStatus.BAD_REQUEST),
    TITLE_URL_IS_NOT_VALID_1(2004, "TitleUrl is not valid", HttpStatus.BAD_REQUEST),
    RATING_IS_NOT_VALID_1(2005, "Rating is not valid", HttpStatus.BAD_REQUEST),
    RATING_IS_NOT_VALID_2(2006, "Rating is not valid", HttpStatus.BAD_REQUEST),
    LOGIN_EMAIL_ERROR(3000, "Login email error", HttpStatus.BAD_REQUEST),
    LOGIN_PASSWORD_ERROR(3001, "Login password error", HttpStatus.BAD_REQUEST),
    INPUT_USERNAME_ERROR(3002, "Input username error", HttpStatus.BAD_REQUEST),
    INPUT_EMAIL_ERROR(3003, "Input email error", HttpStatus.BAD_REQUEST),
    INPUT_PASSWORD_ERROR(3004, "Input password error", HttpStatus.BAD_REQUEST),
    INPUT_AVATAR_URL_ERROR(3005, "Input avatar url error", HttpStatus.BAD_REQUEST),
    INPUT_DESCRIPTION_ERROR(3006, "Input description error", HttpStatus.BAD_REQUEST),
    YOUTUBER_NOT_FOUND(3007, "Yout not found", HttpStatus.NOT_FOUND),
    PASSWORD_NOT_MATCH(3008, "Password not match/Unauthenticated", HttpStatus.UNAUTHORIZED),
    NO_PERMISSION(3009, "No permission/Unauthorized", HttpStatus.FORBIDDEN),
    ROLE_NOT_FOUND(4000, "Role not found", HttpStatus.NOT_FOUND),
    PERMISSION_NOT_FOUND(4001, "Permission not found", HttpStatus.NOT_FOUND),;



    private int code;
    private String message;
    private HttpStatus status;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

}
