package com.testApi.demoApi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Video")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", columnDefinition = "NVARCHAR(50)")
    private String title;

    @Column(name = "description", columnDefinition = "NVARCHAR(100)")
    private String description;

    @Column(name = "videoUrl", columnDefinition = "VARCHAR(2000)")
    private String videoUrl;

    @Column(name = "titleUrl", columnDefinition = "VARCHAR(2000)")
    private String titleUrl;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "releaseDate")
    private LocalDate releaseDate;

    @JoinColumn(name = "youtuber_id")
    @ManyToOne
    private Youtuber youtuber;
}
