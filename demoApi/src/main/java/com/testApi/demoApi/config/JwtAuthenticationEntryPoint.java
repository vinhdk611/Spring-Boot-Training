package com.testApi.demoApi.config;

import com.testApi.demoApi.dto.ApiResponse;
import com.testApi.demoApi.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.PASSWORD_NOT_MATCH;

        //
        response.setStatus(errorCode.getStatus().value());

        //header Content-Type: application/json để trả về kiểu json
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        //thư viện jackson
        ObjectMapper mapper = new ObjectMapper();

        // 1. Serialize apiResponse thành chuỗi JSON và nạp vào bộ đệm RAM (xác định nội dung Response Body)
        response.getWriter().write(mapper.writeValueAsString(apiResponse));

        // 2. Ép xả bộ đệm RAM, chốt Response (Committed) và gửi dữ liệu qua mạng TCP đến Client(cái này ko cần lắm  vì write() nó cũng có gọi rồi)
        response.flushBuffer();
    }
}