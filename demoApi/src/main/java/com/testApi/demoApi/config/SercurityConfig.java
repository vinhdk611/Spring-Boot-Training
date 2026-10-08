package com.testApi.demoApi.config;

import com.testApi.demoApi.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration /*annotation dùng để báo với spring rằng đây là  nơi khai báo các đối tượng duy nhất singleton
và spring cần lưu chúng vào ioc container
*/
@EnableWebSecurity //đã tự động enable rồi, ko cần
public class SercurityConfig {
    // [Kiểu biến]: String (java.lang.String)
    // [Annotation]: @Value (org.springframework.beans.factory.annotation.Value)
    // [Mục đích]: Lưu chuỗi khóa bí mật (Secret Key) lấy từ file application.properties / application.yml
    //             Chuỗi này dùng để ký và giải mã/xác minh chữ ký của JWT Token.
    @Value("${app.signer-key}")
    protected String signerKey;

    // [Kiểu biến]: Array String (java.lang.String[])
    // [Mục đích]: Lưu danh sách các đường dẫn URL API công khai (Public Endpoints) mà không yêu cầu người dùng phải đăng nhập/có Token.
    private final String[] PUBLIC_ENDPOINTS = {"/api/user/login", "/api/user/intro"};

    /**
     SecurityFilterChain : Trả về một chuỗi các Filter bảo mật (Filter Chain) đã được lắp ráp cấu hình để Spring Security áp dụng vào mọi HTTP Request.
     http - HttpSecurity : Là đối tượng Builder chính của Spring Security, cung cấp các API (dạng DSL) để cấu hình phân quyền, CSRF, OAuth2 Resource Server,...
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // =========================================================================
        // 1. PHÂN QUYỀN LUỒNG REQUEST (Authorization Rules)
        // =========================================================================
        // Luồng ngầm: Sau khi xác thực xong (ở bước 2 bên dưới), Request sẽ đi tới
        // 'AuthorizationFilter'. Filter này truy vấn SecurityContextHolder để lấy đối tượng
        // Authentication đã được xác thực thành công và kiểm tra danh sách GrantedAuthority.
        http.authorizeHttpRequests(
                // requests - AuthorizeHttpRequestsConfigurer: Đối tượng Registry giúp đăng ký và ánh xạ các tập luật phân quyền cho URL / HTTP Method.
                requests -> requests
                        // requestMatchers(): Chỉ định quy tắc cho phương thức POST đến các đường dẫn "/user/login", "/user/intro".

                        // permitAll(): Cho phép tất cả mọi người (kể cả chưa đăng nhập) truy cập vào các đường dẫn trên.
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                        /*
                        - hasAuthority(): hàm so sánh chuỗi với role từ claim scope của đối tượng Authentication - là JwtAuthenticationToken, đối tương tạo từ JWT đóng gói
                        - thường sẽ có dạng là SCOPE_... role gì đó, nen neu ko config gi thi phai dung hasAuthority("SCOPE_ADMIN")
                        - muốn nó ko dùng chữ SCOPE nữa thì cấu hình lại JWTAuthenticationConverter
                        - hasRole(): nó là hasAuthority("ROLE_...") --> phuơng thức kiểu role-based
                         */
                        //.requestMatchers(HttpMethod.GET, "/api/video").hasRole(Role.ADMIN.name())
                        // anyRequest(): Đại diện cho TẤT CẢ các HTTP Request còn lại ngoài danh sách PUBLIC_ENDPOINTS.
                        // authenticated(): Bắt buộc các request còn lại phải được xác thực (phải truyền JWT Token hợp lệ).
                        .anyRequest().authenticated()
                        
        );

        // =========================================================================
        // 2. CẤU HÌNH XÁC THỰC OAUTH2 RESOURCE SERVER (CƠ CHẾ NGẦM HOẠT ĐỘNG TẠI ĐÂY)
        // =========================================================================
        /*
         * khi bạn gọi .oauth2ResourceServer(oauth2 -> oauth2.jwt(...)):
         *
         * [BƯỚC NGẦM 1: CHÈN FILTER BẢO MẬT]
         * Spring Security tự động kích hoạt và chèn 'BearerTokenAuthenticationFilter' vào chuỗi SecurityFilterChain.
         * Khi HTTP Request tới (vd: GET /api/video), BearerTokenAuthenticationFilter sẽ:
         *   a. Trích xuất chuỗi Token từ Header: "Authorization: Bearer <token_string>"
         *   b. Đóng gói chuỗi text này thành đối tượng chưa xác thực: 'BearerTokenAuthenticationToken'
         *   c. Chuyển đối tượng này cho 'AuthenticationManager' (cụ thể là ProviderManager).
         *
         * [BƯỚC NGẦM 2: GỌI PROVIDER XÁC THỰC]
         * 'ProviderManager' tìm Provider phù hợp và chuyển giao nhiệm vụ cho 'JwtAuthenticationProvider'.
         * 'JwtAuthenticationProvider' sẽ tự động làm 2 việc:
         *   a. Gọi Bean 'jwtDecoder()' (được bạn khai báo bên dưới) để verify chữ ký HMAC-SHA256 & kiểm tra hạn Expiration.
         *   b. Nếu Token hợp lệ, nó sẽ chuyển đổi kết quả 'Jwt' thu được qua Bean 'jwtAuthenticationConverter()'
         *      để build danh sách Quyền (GrantedAuthority).
         *
         * [BƯỚC NGẦM 3: KHỞI TẠO ĐỐI TƯỢNG AUTHENTICATION HOÀN CHỈNH]
         * 'JwtAuthenticationConverter' tự động khởi tạo đối tượng:
         *   Authentication authentication = new JwtAuthenticationToken(jwt, authorities);
         * (Lưu ý: Lúc này cờ isAuthenticated đã là true).
         *
         * [BƯỚC NGẦM 4: LƯU VÀO THREAD-LOCAL]
         * 'BearerTokenAuthenticationFilter' nhận đối tượng 'JwtAuthenticationToken' trả về và gọi lệnh:
         *   SecurityContextHolder.getContext().setAuthentication(authentication);
         *
         * Sau bước này, Request mới chính thức được chuyển tiếp tới Controller của bạn!
         */
        http.oauth2ResourceServer(
                // oauth2 - OAuth2ResourceServerConfigurer<HttpSecurity>: Đối tượng cấu hình cho vai trò Resource Server (nơi chứa API cần bảo vệ bằng OAuth2/JWT).
                oauth2 -> oauth2
                        .jwt(  // <--- B1: chỗ này new BearerTokenAuthenticationFilter
                        // jwtConfigurer - OAuth2ResourceServerConfigurer<HttpSecurity>.JwtConfigurer:
                        // Đối tượng chuyên cấu hình các chi tiết kỹ thuật cho JWT (như cài đặt Decoder, Authority Converter,...).
                        jwtConfigurer -> jwtConfigurer
                                .decoder(// Gọi hàm jwtDecoder() bên dưới để truyền Bean bộ giải mã JWT vào
                                jwtDecoder()) // <--- B2: Truyền decoder vào để tạo Provider
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())

                )
                        //dùng để xử lý khi xác thực sai thì sẽ làm gì
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())

        );

        // =========================================================================
        // 3. VÔ HIỆU HÓA TÍNH NĂNG BẢO VỆ CSRF (Cross-Site Request Forgery)
        // =========================================================================
        // [Kiểu tham chiếu]: AbstractHttpConfigurer (org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer)
        // [Mục đích]: Dùng Method Reference (AbstractHttpConfigurer::disable) để tắt CSRF.
        //              Do REST API xác thực bằng JWT mang tính Stateless (không dùng Cookie/Session), việc tắt CSRF giúp tránh bị chặn vô lý lỗi 403.
        http.csrf(AbstractHttpConfigurer::disable);

        // [Mục đích .build()]: Thực thi quá trình lắp ráp (build) đối tượng HttpSecurity và trả về SecurityFilterChain hoàn chỉnh.
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    /**
     * PHƯƠNG THỨC TẠO BEAN GIẢI MÃ VÀ XÁC MINH JWT (JwtDecoder)
     *
     * [Kiểu trả về]: JwtDecoder (org.springframework.security.oauth2.jwt.JwtDecoder - Interface)
     * [Mục đích trả về]: Cung cấp một đối tượng có khả năng nhận chuỗi Token dạng Text ("eyJhbGci..."),
     *                    kiểm tra tính hợp lệ của chữ ký, kiểm tra thời hạn (Expiration) và đọc thông tin Payload bên trong.
     */
    @Bean
    public JwtDecoder jwtDecoder() {

        // SecretKeySpec: Chuyển đổi mảng byte của chuỗi 'signerKey' thành một đối tượng khóa mã hóa chuẩn của Java (SecretKey)
        //                            tương thích với thuật toán mã hóa đối xứng HMAC-SHA256 ("HS256").
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS256");

        // NimbusJwtDecoder : Lớp triển khai cụ thể của JwtDecoder (dựa trên thư viện Nimbus JOSE).
        //              Nó dùng 'secretKeySpec' và thuật toán 'HS256' để thực hiện giải mã và verify chữ ký của JWT Token mỗi khi có request gửi lên.
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec) // Truyền khóa mã hóa Java vào Builder
                .macAlgorithm(MacAlgorithm.HS256) // [Kiểu]: MacAlgorithm Enum -> Chỉ định thuật toán HMAC SHA-256
                .build(); // Tạo đối tượng NimbusJwtDecoder
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
