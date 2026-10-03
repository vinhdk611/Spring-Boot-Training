package com.testApi.demoApi.exception;

public class AppException extends RuntimeException {

    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage()); //truyen message len constructor cha, de neu muon in ra message thi van in duoc
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
