package com.testApi.demoApi.config;

import com.testApi.demoApi.entity.Video;
import com.testApi.demoApi.entity.Youtuber;
import com.testApi.demoApi.enums.Country;
import com.testApi.demoApi.enums.Role;
import com.testApi.demoApi.repository.VideoRepository;
import com.testApi.demoApi.repository.YoutuberRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

@RequiredArgsConstructor
@Configuration
public class ApplicationInitConfig {

    @Autowired
    PasswordEncoder passwordEncoder;

    //duoc khoi chay moi khi app duoc run
    @Transactional
    @Bean
    public ApplicationRunner applicationRunner(YoutuberRepository youtuberRepository, VideoRepository videoRepository) {
        return args -> {
            Youtuber youtuber = null;
            if(!youtuberRepository.existsByUsername("admin")){
                youtuber =  Youtuber.builder()
                        .roles(Role.ADMIN.name())
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("123"))
                        .username("admin")
                        .avatarUrl("https://play-lh.googleusercontent.com/JRahekPoLingEC-HHHotGAtiQsV1-O3K6qld0-cqKJkP_dPTkGi90Iiii_S258XR5QeP9oUt1FFnmAZojwqYRg=w240-h480-rw")
                        .country(Country.AMERICA)
                        .description("Im verity!!")
                        .displayName("VERITY FROM MINECRAFT")
                        .build();
            }
            if(videoRepository.findAll().isEmpty() && youtuber != null){
                ArrayList<Video> videos = new ArrayList<>();
                Video vid1 = Video.builder()
                        .title("Tiki Tiki")
                        .description("Mit Khong Thong Minh")
                        .videoUrl("https://www.youtube.com/watch?v=aSGeQA5eRqw&list=RDaSGeQA5eRqw&start_radio=1")
                        .titleUrl("https://media.tenor.com/ApKG4pa06cQAAAAM/red-hearing-peak.gif")
                        .rating(5)
                        .youtuber(youtuber)
                        .releaseDate(LocalDate.now())
                        .build();
                Video vid2 = Video.builder()
                        .title("Yara Yara")
                        .description("Mit Khong Thong Minh")
                        .videoUrl("https://www.youtube.com/watch?v=-N5oZH74wEA&list=RD-N5oZH74wEA&start_radio=1")
                        .titleUrl("https://i.namu.wiki/i/bqKmbkSyipsh0kx1oN_sYii6tJ7bbZgg7qAAT5rmi54VZ9EC9T4e58QHRmFgw7ZNtH8iWrnE--0rtimMfVpPjg.webp")
                        .rating(5)
                        .youtuber(youtuber)
                        .releaseDate(LocalDate.now())
                        .build();
                Video vid3 = Video.builder()
                        .title("MONTAGEM SUPERSONIC")
                        .description("Mit Khong Thong Minh")
                        .videoUrl("https://www.youtube.com/watch?v=vMCe31m964A&list=RDaSGeQA5eRqw&index=18")
                        .titleUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRZFn4k8plV7Z2DaXuSgCWoskwwoDhk1JLU3kAIJnG-YQ&s")
                        .rating(3)
                        .youtuber(youtuber)
                        .releaseDate(LocalDate.now())
                        .build();
                videos.add(vid1);
                videos.add(vid2);
                videos.add(vid3);
                youtuber.setVideos(videos);
                youtuberRepository.save(youtuber);
                videoRepository.save(vid1);
                videoRepository.save(vid2);
                videoRepository.save(vid3);
            }
        };
    }
}
