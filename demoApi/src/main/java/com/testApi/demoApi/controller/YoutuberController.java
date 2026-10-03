package com.testApi.demoApi.controller;

import com.testApi.demoApi.dto.ApiResponse;
import com.testApi.demoApi.dto.youtuberDto.*;
import com.testApi.demoApi.entity.Youtuber;
import com.testApi.demoApi.service.YoutuberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class YoutuberController {

    @Autowired
    private final YoutuberService youtuberService;

    public YoutuberController(YoutuberService youtuberService) {
        this.youtuberService = youtuberService;
    }

    @GetMapping
    public ApiResponse<List<YoutuberResponse>> getYoutubers() {
        return ApiResponse.<List<YoutuberResponse>>builder()
                .code(200)
                .message("List youtubers")
                .result(youtuberService.getAllYoutubers())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<YoutuberResponse> getYoutuber(@PathVariable Long id) {
        return ApiResponse.<YoutuberResponse>builder()
                .code(200)
                .message("Youtuber with id " + id)
                .result(youtuberService.getYoutuberById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<YoutuberResponse> createYoutuber(@RequestBody @Valid AddYoutuberRequest request) {
        return ApiResponse.<YoutuberResponse>builder()
                .code(201)
                .message("Youtuber created")
                .result(youtuberService.saveYoutuber(null, request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<YoutuberResponse> updateYoutuber(@RequestBody @Valid AddYoutuberRequest request, @PathVariable Long id) {
        return ApiResponse.<YoutuberResponse>builder()
                .code(200)
                .message("Updated Youtuber with id " + id)
                .result(youtuberService.saveYoutuber(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteYoutuber(@PathVariable Long id) {
        youtuberService.deleteYoutuber(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Deleted Youtuber with id " + id)
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ApiResponse.<LoginResponse>builder()
                .code(201)
                .message("Login successful")
                .result(youtuberService.login(request))
                .build();
    }

    @PostMapping
    public ApiResponse<IntrospectResponse> introspect(@RequestBody @Valid IntrospectRequest request) {
        return ApiResponse.<IntrospectResponse>builder()
                .code(201)
                .message("Introspect successful")
                .result(youtuberService.introspectToken(request))
                .build();
    }
}
