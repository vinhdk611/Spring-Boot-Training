package com.testApi.demoApi.dto.youtuberDto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
public class IntrospectRequest {
    String token;
}
