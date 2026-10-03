package com.testApi.demoApi.repository;

import com.testApi.demoApi.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video,Integer> {
}
