package com.testApi.demoApi.service;

import com.testApi.demoApi.dto.youtuberDto.*;
import jakarta.validation.Valid;

import java.util.List;

public interface YoutuberService {
    List<YoutuberResponse> getAllYoutubers();

    YoutuberResponse getYoutuberById(Long id);

    YoutuberResponse saveYoutuber(Long id, AddYoutuberRequest request);

    void deleteYoutuber(Long id);

    LoginResponse login(LoginRequest request);

    IntrospectResponse introspectToken(@Valid IntrospectRequest request);

    YoutuberResponse getContextHolderYoutuber();
}
