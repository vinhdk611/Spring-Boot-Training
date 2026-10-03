package com.testApi.demoApi.exception;

public enum ErrorCode {
    VIDEO_EXISTED(1001, "User existed"),
    VIDEO_NOT_FOUND(1002, "Video not found"),
    UNKNOWN_ERROR(999, "Unknown error"),
    TITLE_IS_NOT_VALID_1(2001, "Title is not valid"),
    INVALID_KEY(1004, "Invalid message key"),
    DESCRIPTION_IS_NOT_VALID_1(2002, "Description is not valid"),
    VIDEO_URL_IS_NOT_VALID_1(2003, "Video is not valid"),
    TITLE_URL_IS_NOT_VALID_1(2004, "TitleUrl is not valid"),
    RATING_IS_NOT_VALID_1(2005, "Rating is not valid"),
    RATING_IS_NOT_VALID_2(2006, "Rating is not valid"),
    LOGIN_EMAIL_ERROR(3000, "Login email error"),
    LOGIN_PASSWORD_ERROR(3001, "Login password error"),
    INPUT_USERNAME_ERROR(3002, "Input username error"),
    INPUT_EMAIL_ERROR(3003, "Input email error"),
    INPUT_PASSWORD_ERROR(3004, "Input password error"),
    INPUT_AVATAR_URL_ERROR(3005, "Input avatar url error"),
    INPUT_DESCRIPTION_ERROR(3006, "Input description error"),
    YOUTUBER_NOT_FOUND(3007, "Yout not found"),
    PASSWORD_NOT_MATCH(3008, "Password not match"),;


    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
