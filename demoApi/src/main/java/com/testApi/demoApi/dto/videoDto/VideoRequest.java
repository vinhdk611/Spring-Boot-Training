package com.testApi.demoApi.dto.videoDto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter //tu dong tao getter cho tat ca field
@Setter //tu dong tao setter cho tat ca field
@AllArgsConstructor //tu dong tao contructor full tham so
@NoArgsConstructor // tu dong tao constructor rong
/*
@Data: bao gom @Getter, @Setter, @RequireArgsConstructor, @ToString và @EqualsAndHashCode
@Builder: cung cap ham builder va cho phep . lien tuc de set cac field gia tri
 */
@Builder
public class VideoRequest {

    @Size(min = 1, message = "TITLE_IS_NOT_VALID_1")
    private String title;

    @Size(min = 1, message = "DESCRIPTION_IS_NOT_VALID_1")
    private String description;

    @Size(min = 1, message = "VIDEO_URL_IS_NOT_VALID_1")
    private String videoUrl;

    @Size(min = 1, message = "TITLE_URL_IS_NOT_VALID_1")
    private String titleUrl;

    @Min(value = 1, message = "RATING_IS_NOT_VALID_1")
    @Max(value = 5, message = "RATING_IS_NOT_VALID_2")
    private Integer rating;

}
