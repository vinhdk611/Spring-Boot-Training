package com.testApi.demoApi.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.testApi.demoApi.dto.youtuberDto.*;
import com.testApi.demoApi.entity.Role;
import com.testApi.demoApi.enums.Country;
import com.testApi.demoApi.entity.Youtuber;
import com.testApi.demoApi.exception.AppException;
import com.testApi.demoApi.exception.ErrorCode;
import com.testApi.demoApi.mapper.RoleMapper;
import com.testApi.demoApi.mapper.VideoMapper;
import com.testApi.demoApi.mapper.YoutuberMapper;
import com.testApi.demoApi.repository.RoleRepository;
import com.testApi.demoApi.repository.YoutuberRepository;
import com.testApi.demoApi.service.YoutuberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class YoutuberServiceImpl implements YoutuberService {

    //dung nonfinal de cai field nay ko bi tu inject vao constructor
    @Value("${app.signer-key}")
    protected String signerKey;

    private final YoutuberRepository youtuberRepository;
    private final YoutuberMapper youtuberMapper;
    private final VideoMapper videoMapper;
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;


    @Transactional(readOnly = true)
    @Override
    public List<YoutuberResponse> getAllYoutubers() {
        List<Youtuber> youtubers = youtuberRepository.findAll();
        return youtubers.stream().map(youtuber -> {
            YoutuberResponse youtuberResponse = youtuberMapper.toResponse(youtuber);
            youtuberResponse.setVideos(youtuber.getVideos().stream().map(videoMapper::toVideoResponse).toList());
            return youtuberResponse;
        }).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public YoutuberResponse getYoutuberById(Long id) {
        Youtuber youtuber = youtuberRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.YOUTUBER_NOT_FOUND));
        YoutuberResponse youtuberResponse = youtuberMapper.toResponse(youtuber);
        youtuberResponse.setVideos(youtuber.getVideos().stream().map(videoMapper::toVideoResponse).toList());
        return youtuberResponse;
    }

    @Transactional
    @Override
    public YoutuberResponse saveYoutuber(Long id, AddYoutuberRequest request) {
        Youtuber youtuber;
        //10 ở đây là độ mạnh của mk
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findById("STAFF").orElseThrow(
                () -> new AppException(ErrorCode.ROLE_NOT_FOUND)
        ));
        if (id == null) {
            youtuber = new Youtuber();
            youtuberMapper.toYoutuber(request, youtuber);
            youtuber.setDisplayName(createDisplayName(youtuber.getUsername()));
        } else {
            youtuber = youtuberRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.YOUTUBER_NOT_FOUND));

            youtuberMapper.toYoutuber(request, youtuber);
            if(!request.getRoles().isEmpty()){
                request.getRoles().forEach(roleId -> {
                    Role  role = roleRepository.findById(roleId).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
                    roles.add(role);
                });
            }
        }
        youtuber.setRoles(roles);
        youtuber.setCountry(fromName(request.getCountry()));
        youtuber.setPassword(passwordEncoder.encode(request.getPassword()));
        Youtuber savedYoutuber = youtuberRepository.save(youtuber);

        YoutuberResponse youtuberResponse = youtuberMapper.toResponse(savedYoutuber);
        youtuberResponse.setVideos(savedYoutuber.getVideos().stream().map(videoMapper::toVideoResponse).toList());
        youtuberResponse.setRoles(youtuber.getRoles().stream().map(roleMapper::toRoleResponse).collect(Collectors.toSet()));
        return youtuberResponse;
    }

    @Transactional
    @Override
    public void deleteYoutuber(Long id) {
        if (id == null) throw new AppException(ErrorCode.YOUTUBER_NOT_FOUND);
        youtuberRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public LoginResponse login(LoginRequest request) {

        //10 ở đây là độ mạnh của mk
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String inputEmail = request.getEmail();
        if(inputEmail == null || inputEmail.isEmpty()) throw new AppException(ErrorCode.INPUT_USERNAME_ERROR);

        String inputPassword = request.getPassword();
        if(inputPassword == null || inputPassword.isEmpty()) throw new AppException(ErrorCode.INPUT_PASSWORD_ERROR);

        Youtuber youtuber = youtuberRepository.findByEmail(inputEmail).orElseThrow(() -> new AppException(ErrorCode.YOUTUBER_NOT_FOUND));

        if(!passwordEncoder.matches(inputPassword, youtuber.getPassword())) throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);

        return LoginResponse.builder()
                .authenticated(true)
                .token(generateToken(youtuber))
                .build();
    }

    @Override
    public IntrospectResponse introspectToken(IntrospectRequest request) {
        String token = request.getToken();

        try {
            //object dùng khai báo thuật toán trước đó đã dùng để hash Signature để xác thực lại với mã Secret Key
            JWSVerifier jwsVerifier = new MACVerifier(signerKey.getBytes());

            //lớp con của JWSObject, đại diện cho đôi tượng JWT có signature xác định
            SignedJWT signedJWT = SignedJWT.parse(token);

            //verify check token JWT dựa theo secret key và thuật toán mà object JWSVerifier cung cấp
            boolean check = signedJWT.verify(jwsVerifier);

            Date expiredTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            return IntrospectResponse.builder()
                    .valid(check && expiredTime.after(new Date()))
                    .build();

        } catch (JOSEException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public YoutuberResponse getContextHolderYoutuber() {
        /*
        khi request được xác thực thành công, thông tin đăng nhập hay Authentication object sẽ
        được lưu trong securitycontextholder
         */
        SecurityContext context = SecurityContextHolder.getContext();
        //lấy đối tượng context được lưu
        String name = context.getAuthentication().getName();

        Youtuber youtuber = youtuberRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.YOUTUBER_NOT_FOUND)
        );

        return youtuberMapper.toResponse(youtuber);
    }

    private String createDisplayName(String username) {
        if (username == null || username.isBlank()) {
            username = "YouTuber";
        }

        String name = java.util.Arrays.stream(
                        username.trim()
                                .replace("_", " ")
                                .replace("-", " ")
                                .replace(".", " ")
                                .split("\\s+")
                )
                .map(word -> Character.toUpperCase(word.charAt(0))
                        + word.substring(1).toLowerCase())
                .collect(java.util.stream.Collectors.joining(" "));

        int randomNumber = new java.util.Random().nextInt(1000);

        return String.format("%s_%03d", name, randomNumber);
    }

    public static Country fromName(String name) {
        for (Country country : Country.values()) {
            if (country.getName().equalsIgnoreCase(name)) {
                return country;
            }
        }

        throw new IllegalArgumentException("Unknown country: " + name);
    }

    private String generateToken(Youtuber youtuber) {

        var roles = youtuber.getRoles().stream().map(Role::getName).collect(Collectors.toSet());

        //dinh nghia thuat toan duoc su dung
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        // data ben trong body
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(youtuber.getUsername()) //đại diện user đăng nhập
                .issuer("VinhDinh") //xac dinh token duoc issuer tu ai, thuong la domain service
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope", roles) //tự tạo field cho object JWT, tạo scope để token có role
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        //đối tượng lưu trữ JWT gồm header, payload và signature, cần 2 param là header và payload
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            /*hàm sign sẽ tạo Signature, là chữ ký tạo bởi thuật toán(ở đây đang dùng thuật toán HMAC - MACSigner)
            thuật toán sẽ hash header, payload và secret key
             */
            jwsObject.sign(new MACSigner(signerKey));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

}
