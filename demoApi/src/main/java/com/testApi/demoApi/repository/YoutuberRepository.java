package com.testApi.demoApi.repository;

import com.testApi.demoApi.entity.Youtuber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface YoutuberRepository extends JpaRepository<Youtuber, Long> {
    Optional<Youtuber> findByEmail(String email);

    boolean existsByUsername(String username);
}
