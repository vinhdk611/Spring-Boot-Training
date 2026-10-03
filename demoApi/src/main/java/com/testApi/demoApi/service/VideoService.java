package com.testApi.demoApi.service;

import com.testApi.demoApi.dto.videoDto.VideoRequest;
import com.testApi.demoApi.dto.videoDto.VideoResponse;

import java.util.List;

public interface VideoService {

    public List<VideoResponse> getList() ;

    public VideoResponse getById(Integer id) ;

    public List<VideoResponse> getListByName(String name) ;

    public VideoResponse save(Integer id, VideoRequest videoRequest) ;

    public String delete(Integer id) ;
}
