package com.testApi.demoApi.dto.youtuberDto;


import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddYoutuberRequest {

    @Size(min = 1, max = 50, message = "INPUT_USERNAME_ERROR")
    String username;

    @Size(min = 1, max = 50, message = "INPUT_EMAIL_ERROR")
    String email;

    @Size(min = 1, max = 50, message = "INPUT_PASSWORD_ERROR")
    String password;

    @Size(min = 1, max = 2000, message = "INPUT_AVATAR_URL_ERROR")
    String avatarUrl;

    @Size(min = 1, max = 100, message = "INPUT_DESCRIPTION_ERROR")
    String description;

    String country;

    Set<String> roles;
}
