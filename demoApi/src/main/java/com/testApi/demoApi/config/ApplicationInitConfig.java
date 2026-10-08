package com.testApi.demoApi.config;

import com.testApi.demoApi.entity.Permission;
import com.testApi.demoApi.entity.Role;
import com.testApi.demoApi.entity.Video;
import com.testApi.demoApi.entity.Youtuber;
import com.testApi.demoApi.enums.Country;
import com.testApi.demoApi.repository.PermissionRepository;
import com.testApi.demoApi.repository.RoleRepository;
import com.testApi.demoApi.repository.VideoRepository;
import com.testApi.demoApi.repository.YoutuberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class ApplicationInitConfig {

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Bean
    public ApplicationRunner applicationRunner(
            YoutuberRepository youtuberRepository,
            VideoRepository videoRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository) {
        return args -> {
            if (roleRepository.count() == 0 || permissionRepository.count() == 0) {
                Permission manageVideoUC = Permission.builder().name("MANAGE_VIDEO").description("Actor can CRUD video").build();
                Permission manageYoutuberUC = Permission.builder().name("MANAGE_YOUTUBER").description("Actor can CRUD youtuber").build();
                Permission viewProfileUC = Permission.builder().name("VIEW_PROFILE").description("Actor can view his own profile").build();
                Permission manageRole = Permission.builder().name("MANAGE_ROLE").description("Actor can CRUD role").build();
                Permission managePermission = Permission.builder().name("MANAGE_PERMISSION").description("Actor can CRUD permission").build();
                Permission authentication = Permission.builder().name("AUTHENTICATION").description("Actor can login/logout").build();

                permissionRepository.saveAll(List.of(
                        manageVideoUC, manageYoutuberUC, viewProfileUC,
                        manageRole, managePermission, authentication
                ));

                Role adminRole = Role.builder()
                        .name("ADMIN")
                        .description("Admin manages master data")
                        .permissions(Set.of(manageRole, managePermission, manageVideoUC, manageYoutuberUC, viewProfileUC, authentication))
                        .build();

                Role staffRole = Role.builder()
                        .name("STAFF")
                        .description("Staff can authenticate and view profile")
                        .permissions(Set.of(viewProfileUC, authentication))
                        .build();

                roleRepository.saveAll(List.of(adminRole, staffRole));
            }

            Youtuber adminUser = youtuberRepository.findByUsername("admin")
                    .orElseGet(() -> {
                        Role adminRole = roleRepository.findById("ADMIN").orElse(null);
                        Role staffRole = roleRepository.findById("STAFF").orElse(null);

                        Set<Role> roles = new java.util.HashSet<>();
                        if (adminRole != null) roles.add(adminRole);
                        if (staffRole != null) roles.add(staffRole);

                        Youtuber newAdmin = Youtuber.builder()
                                .roles(roles)
                                .email("admin@gmail.com")
                                .password(passwordEncoder.encode("123"))
                                .username("admin")
                                .avatarUrl("https://play-lh.googleusercontent.com/JRahekPoLingEC-HHHotGAtiQsV1-O3K6qld0-cqKJkP_dPTkGi90Iiii_S258XR5QeP9oUt1FFnmAZojwqYRg=w240-h480-rw")
                                .country(Country.AMERICA)
                                .description("Im verity!!")
                                .displayName("VERITY FROM MINECRAFT")
                                .build();

                        return youtuberRepository.save(newAdmin);
                    });

            if (videoRepository.count() == 0 && adminUser != null) {
                Video vid1 = Video.builder()
                        .title("Tiki Tiki")
                        .description("Mit Khong Thong Minh")
                        .videoUrl("https://www.youtube.com/watch?v=aSGeQA5eRqw&list=RDaSGeQA5eRqw&start_radio=1")
                        .titleUrl("https://media.tenor.com/ApKG4pa06cQAAAAM/red-hearing-peak.gif")
                        .rating(5)
                        .youtuber(adminUser)
                        .releaseDate(LocalDate.now())
                        .build();

                Video vid2 = Video.builder()
                        .title("Yara Yara")
                        .description("Mit Khong Thong Minh")
                        .videoUrl("https://www.youtube.com/watch?v=-N5oZH74wEA&list=RD-N5oZH74wEA&start_radio=1")
                        .titleUrl("https://i.namu.wiki/i/bqKmbkSyipsh0kx1oN_sYii6tJ7bbZgg7qAAT5rmi54VZ9EC9T4e58QHRmFgw7ZNtH8iWrnE--0rtimMfVpPjg.webp")
                        .rating(5)
                        .youtuber(adminUser)
                        .releaseDate(LocalDate.now())
                        .build();

                Video vid3 = Video.builder()
                        .title("MONTAGEM SUPERSONIC")
                        .description("Mit Khong Thong Minh")
                        .videoUrl("https://www.youtube.com/watch?v=vMCe31m964A&list=RDaSGeQA5eRqw&index=18")
                        .titleUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRZFn4k8plV7Z2DaXuSgCWoskwwoDhk1JLU3kAIJnG-YQ&s")
                        .rating(3)
                        .youtuber(adminUser)
                        .releaseDate(LocalDate.now())
                        .build();

                videoRepository.saveAll(List.of(vid1, vid2, vid3));
            }
        };
    }
}