package com.testApi.demoApi.dto.youtuberDto;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    @Size(min = 1, max = 50, message = "LOGIN_EMAIL_ERROR")
    String email;

    @Size(min = 1, max = 50, message = "LOGIN_PASSWORD_ERROR")
    String password;
}
