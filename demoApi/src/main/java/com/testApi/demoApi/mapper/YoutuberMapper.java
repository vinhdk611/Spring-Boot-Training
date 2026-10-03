package com.testApi.demoApi.mapper;

import com.testApi.demoApi.dto.youtuberDto.AddYoutuberRequest;
import com.testApi.demoApi.dto.youtuberDto.YoutuberResponse;
import com.testApi.demoApi.entity.Youtuber;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface YoutuberMapper {

    @Mapping(target = "videos", ignore = true)
    @Mapping(target = "country", source = "country.name")
    YoutuberResponse toResponse(Youtuber youtuber);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "displayName", ignore = true)
    @Mapping(target = "videos", ignore = true)
    @Mapping(target = "country", ignore = true)
    void toYoutuber(AddYoutuberRequest request, @MappingTarget Youtuber youtuber);
}
