package com.testApi.demoApi.mapper;

import com.testApi.demoApi.dto.videoDto.VideoRequest;
import com.testApi.demoApi.dto.videoDto.VideoResponse;
import com.testApi.demoApi.entity.Video;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring") //bao mapstruct biet rang generate mapper nay theo kieu spring -- dependency injection
public interface VideoMapper {

    VideoResponse toVideoResponse(Video video);

    // 1. Dùng khi tạo mới (id để JPA tự gen, releaseDate lấy ngày hiện tại)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "releaseDate", expression = "java(java.time.LocalDate.now())") // map releaseDate cua video la LocalDate.now()
    Video toVideo(VideoRequest request);

    // 2. Dùng khi Cập nhật (đè data từ request lên video đã có, giữ nguyên id & releaseDate)
    @Mapping(target = "id", ignore = true) //khai bao de ko map id va releaseDate
    @Mapping(target = "releaseDate", ignore = true)
    void updateVideoFromRequest(VideoRequest request, @MappingTarget Video video);
}
