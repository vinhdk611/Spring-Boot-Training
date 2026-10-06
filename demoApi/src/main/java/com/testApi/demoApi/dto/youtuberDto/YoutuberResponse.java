package com.testApi.demoApi.dto.youtuberDto;

import com.testApi.demoApi.dto.videoDto.VideoResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class YoutuberResponse {
    Long id;
    String username;
    String email;
    String displayName;
    String avatarUrl;
    String description;
    String country;
    List<VideoResponse> videos;
    String roles;
}
