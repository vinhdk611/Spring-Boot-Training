package com.testApi.demoApi.exception;

import com.testApi.demoApi.dto.ApiResponse;
import org.antlr.v4.runtime.atn.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = MethodArgumentNotValidException.class) //class exception muốn bắt
    ResponseEntity<ApiResponse> handlingTitleError(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getFieldError().getDefaultMessage();
        ErrorCode errorCode;
        try{
            errorCode = ErrorCode.valueOf(errorMsg);
        }
        catch (Exception ex){
            errorCode = ErrorCode.INVALID_KEY;
        }
        return ResponseEntity.status(errorCode.getStatus()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    };

    @ExceptionHandler(value = Exception.class) //class exception muốn bắt
    ResponseEntity<ApiResponse> handlingUnknownException(Exception e) {
        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .code(ErrorCode.UNKNOWN_ERROR.getCode())
                        .message(ErrorCode.UNKNOWN_ERROR.getMessage())
                        .build());
    };

    @ExceptionHandler(value = RuntimeException.class) //class exception muốn bắt
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage())
                        .build()
        );
    };

    @ExceptionHandler(value = AppException.class) //class exception muốn bắt
    ResponseEntity<ApiResponse> handlingAppException(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        //ko co ham handler nay ma dung ham handler cha --> ko lay duoc errorCode
        return ResponseEntity.status(errorCode.getStatus()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    };

    //dùng để handler khi vi phạm có quyền(ở mấy  chỗ check hasRole hay hasAuthorites )
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException e) {
        ErrorCode errorCode = ErrorCode.NO_PERMISSION;

        return ResponseEntity.status(errorCode.getStatus()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );
    }



}
