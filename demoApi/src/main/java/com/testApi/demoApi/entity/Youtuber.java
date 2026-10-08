package com.testApi.demoApi.entity;

import com.testApi.demoApi.enums.Country;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Youtuber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(columnDefinition = "NVARCHAR(50)")
    String username;

    @Column(columnDefinition = "VARCHAR(50)")
    String email;

    @Column(columnDefinition = "VARCHAR(100)")
    String password;

    @Column(name = "displayName", columnDefinition = "NVARCHAR(60)")
    String displayName;

    @Column(name = "avatarUrl", columnDefinition = "VARCHAR(2000)")
    String avatarUrl;

    @Column(columnDefinition = "NVARCHAR(100)")
    String description;

    @Enumerated(EnumType.STRING)
    Country country;

    @ManyToMany
    Set<Role> roles;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "youtuber")
    List<Video> videos = new ArrayList<>();
}
