package com.testApi.demoApi.dto.videoDto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Getter //tu dong tao getter cho tat ca field
@Setter //tu dong tao setter cho tat ca field
@AllArgsConstructor //tu dong tao contructor full tham so
@NoArgsConstructor // tu dong tao constructor rong
/*
@Data: bao gom @Getter, @Setter, @RequireArgsConstructor, @ToString và @EqualsAndHashCode
@Builder: cung cap ham builder va cho phep . lien tuc de set cac field gia tri, giup tao object voi khong phai tat ca field day du, code clean hon
 */
@FieldDefaults(level = AccessLevel.PRIVATE) //tu dong set accesslevel la private, ko can phai khai bao private cho tung cai nua
@Builder
public class VideoResponse {
    Integer id;
    String title;
    String description;
    String videoUrl;
    String titleUrl;
    Integer rating;
    LocalDate releaseDate;

}
