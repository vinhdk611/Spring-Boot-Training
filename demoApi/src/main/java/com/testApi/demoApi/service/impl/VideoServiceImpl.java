package com.testApi.demoApi.service.impl;

import com.testApi.demoApi.dto.videoDto.VideoRequest;
import com.testApi.demoApi.dto.videoDto.VideoResponse;
import com.testApi.demoApi.entity.Video;
import com.testApi.demoApi.exception.AppException;
import com.testApi.demoApi.exception.ErrorCode;
import com.testApi.demoApi.mapper.VideoMapper;
import com.testApi.demoApi.repository.VideoRepository;
import com.testApi.demoApi.service.VideoService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {


    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;

    public VideoServiceImpl(VideoRepository videoRepository, VideoMapper videoMapper) {
        this.videoRepository = videoRepository;
        this.videoMapper = videoMapper;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<VideoResponse> getList() {
        List<Video> videoList = videoRepository.findAll();
        List<VideoResponse> videoResponseList = new ArrayList<>();
        videoResponseList = videoList.stream().map((videoMapper::toVideoResponse)).toList();
        return videoResponseList;
    }
    
    //kiểm tra giá trị trả về (videoResponse) có bằng name của authentication ko
    @PostAuthorize("returnObject.username == authentication.name")
    @Override
    public VideoResponse getById(Integer id) {
        Video video = videoRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VIDEO_NOT_FOUND) );
        VideoResponse videoResponse = videoMapper.toVideoResponse(video);
        return videoResponse;
    }

    @Override
    public List<VideoResponse> getListByName(String name) {

        return List.of();
    }

    @Transactional
    @Override
    public VideoResponse save(Integer id, VideoRequest videoRequest) {
        //old
//        Video video;
//        if(id==null){
//            video = new Video();
//        }
//        else {
//            video = videoRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VIDEO_NOT_FOUND) );
//        }
//        Video newVideo = this.mapToVideo(id, video, videoRequest);
//        videoRepository.save(newVideo);
//        return this.mapToVideoResponse(newVideo);
        //new
        Video video;
        if(id==null){
            video = videoMapper.toVideo(videoRequest);
        }
        else {
            video = videoRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VIDEO_NOT_FOUND) );
            videoMapper.updateVideoFromRequest(videoRequest,video);
        }
        videoRepository.save(video);
        return videoMapper.toVideoResponse(video);

    }

    @Override
    public String delete(Integer id) {
        if(id==null || !videoRepository.existsById(id)){
            throw new AppException(ErrorCode.VIDEO_NOT_FOUND);
        }
        videoRepository.deleteById(id);
        return "ok";
    }


    /*
    MAP DTO-ENTITY
     */
    private VideoResponse mapToVideoResponse(Video video) {
        //old:
//        VideoResponse videoResponse = new VideoResponse();
//        videoResponse.setId(video.getId());
//        videoResponse.setTitle(video.getTitle());
//        videoResponse.setVideoUrl(video.getVideoUrl());
//        videoResponse.setDescription(video.getDescription());
//        videoResponse.setRating(video.getRating());
//        videoResponse.setTitleUrl(video.getTitleUrl());
//        videoResponse.setReleaseDate(video.getReleaseDate());

//new:
        return VideoResponse.builder()
                .id(video.getId())
                .title(video.getTitle())
                .videoUrl(video.getVideoUrl())
                .description(video.getDescription())
                .rating(video.getRating())
                .titleUrl(video.getTitleUrl())
                .releaseDate(video.getReleaseDate())
                .build();
    }

    private Video mapToVideo(Integer id, Video video, VideoRequest videoRequest) {
        if(id != null) video.setId(id);
        else video.setReleaseDate(LocalDate.now());
        video.setTitle(videoRequest.getTitle());
        video.setDescription(videoRequest.getDescription());
        video.setVideoUrl(videoRequest.getVideoUrl());
        video.setTitleUrl(videoRequest.getTitleUrl());
        video.setRating(videoRequest.getRating());
        return video;
    }
}

