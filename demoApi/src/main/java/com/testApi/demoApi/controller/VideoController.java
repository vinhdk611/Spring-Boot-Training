package com.testApi.demoApi.controller;

import com.testApi.demoApi.dto.ApiResponse;
import com.testApi.demoApi.dto.videoDto.VideoRequest;
import com.testApi.demoApi.dto.videoDto.VideoResponse;
import com.testApi.demoApi.service.VideoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/video")
@CrossOrigin(origins = "*")
public class VideoController {
    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping
    public ApiResponse<List<VideoResponse>> getVideo(){

        return ApiResponse.<List<VideoResponse>>builder()
                .code(200)
                .message("Mit Mom thoi")
                .result(videoService.getList())
                .build();
    }

    @GetMapping("/{id}")
    public VideoResponse getVideoById(@PathVariable Integer id) {
        return videoService.getById(id);
    }

    @PostMapping
    public ApiResponse<VideoResponse> addVideo(@RequestBody @Valid VideoRequest videoRequest) {
        ApiResponse<VideoResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(201);
        apiResponse.setMessage("Mit Khong thong minh");
        apiResponse.setResult(videoService.save(null, videoRequest));
        return apiResponse;
    }

    @PutMapping("/{id}")
    public VideoResponse updateVideo(@PathVariable Integer id, @RequestBody @Valid VideoRequest videoRequest) {
        return videoService.save(id, videoRequest);
    }

    @DeleteMapping("{id}")
    public String deleteVideo(@PathVariable Integer id) {
        return videoService.delete(id);
    }
}
