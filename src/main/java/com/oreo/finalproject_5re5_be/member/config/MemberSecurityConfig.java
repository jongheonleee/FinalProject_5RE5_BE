package com.oreo.finalproject_5re5_be.member.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer.SessionFixationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class MemberSecurityConfig {

    private final LoginAuthenticationSuccessHandler successHandler;
    private final LoginAuthenticationFailureHandler failureHandler;
//    private final CorsConfigurationSource corsConfigurationSource;
//
//    public MemberSecurityConfig(LoginAuthenticationSuccessHandler successHandler,
//        LoginAuthenticationFailureHandler failureHandler,
//        CorsConfigurationSource corsConfigurationSource) {
//        this.successHandler = successHandler;
//        this.failureHandler = failureHandler;
//        this.corsConfigurationSource = corsConfigurationSource;
//    }

    private final MemberConfig memberConfig;

    public MemberSecurityConfig(LoginAuthenticationSuccessHandler successHandler,
        LoginAuthenticationFailureHandler failureHandler, MemberConfig memberConfig) {
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.memberConfig = memberConfig;
    }

    // SecurityFilterChain 설정 빈 등록, 추후에 적용 예정(다른 파트 작업 완료후 인증/인가 처리 적용예정)
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // csrf 비활성화
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/", "/api/member/register") // 회원가입 페이지와 홈 페이지는 인증/인가 없이 접근 가능
//                        .permitAll()
//                        .requestMatchers("/audio/**", "/project/**", "/languagecode/**",
//                                "/voice/**", "/style", "/vc/**", "/concat") // 그외의 페이지는 인증/인가가 필요함
//                        .authenticated()
//                )
//                .formLogin(formLogin -> formLogin
//                        .loginPage("/api/member/login") // 로그인 페이지 경로 설정
//                        .successHandler(successHandler) // 로그인 성공시 처리되는 핸들러 설정
//                        .failureUrl("/api/member/login") // 로그인 실패시 로그인 페이지로 이동
//                ).logout(logout -> logout
//                        .logoutUrl("/api/member/logout") // 로그아웃 경로 설정
//                        .invalidateHttpSession(true) // 로그아웃시 세션 무효화 설정
//                        .logoutSuccessUrl("/") // 로그아웃 성공시 이동할 페이지 설정
//                );
//
//
//        return http.build();
//    }
// 밑에 개발 어느정도 마무리 되면 주석처리된 부분 주석 해제하고 적용할 예정
//                        .requestMatchers("/", "/member/**", "/api/member/**", "/api/member-term-condition/**", // 허용되는 URL
//                                         "/audio/**", "/project/**", "/languagecode/**",
//                                         "/voice/**", "/style/**", "/vc/**", "/concat/**",
//                                         "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html") // Swagger 관련 URL 허용

//    // SecurityFilterChain을 빈으로 등록
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // 새로운 방식으로 CORS 설정 적용
//                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
//                .authorizeHttpRequests(authorize -> authorize
//                                .anyRequest() // 개발 단계로 모든 요청 열어둠
//                                .permitAll() // 위 URL들은 인증 없이 접근 가능
//                )
//                .formLogin(formLogin -> formLogin
//                        .loginPage("/api/member/login") // 로그인 페이지 경로 설정
//                        .successHandler(successHandler) // 로그인 성공 시 처리되는 핸들러 설정
//                        .failureHandler(failureHandler) // 로그인 실패 시 로그인 페이지로 이동
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/api/member/logout") // 로그아웃 경로 설정
//                        .invalidateHttpSession(true) // 로그아웃 시 세션 무효화
//                        .logoutSuccessUrl("/") // 로그아웃 성공 시 이동할 페이지
//                );
//
//        return http.build();
//    }


    // SecurityFilterChain을 빈으로 등록
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(
                memberConfig.corsConfigurationSource())); // 새로운 방식으로 CORS 설정 적용
        http
            .csrf(AbstractHttpConfigurer::disable); // CSRF 비활성화
        http
            .sessionManagement(sessionManagement -> sessionManagement.sessionFixation(
                    SessionFixationConfigurer::changeSessionId)
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)); // 기본값
        http
            .formLogin(formLogin -> formLogin
                .loginPage("/api/member/login") // 로그인 페이지 경로 설정
                .successHandler(successHandler) // 로그인 성공 시 처리되는 핸들러 설정
                .failureHandler(failureHandler) // 로그인 실패 시 로그인 페이지로 이동
            );
        http
            .httpBasic(
                AbstractHttpConfigurer::disable); // HTTP Basic 인증 비활성화
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/").permitAll()
                .anyRequest() // 개발 단계로 모든 요청 열어둠
                .permitAll() // 위 URL들은 인증 없이 접근 가능
            );
        http
            .logout(logout -> logout
                .logoutUrl("/api/member/logout") // 로그아웃 경로 설정
                .invalidateHttpSession(true) // 로그아웃 시 세션 무효화
                .logoutSuccessUrl("/") // 로그아웃 성공 시 이동할 페이지
            );

        return http.build();
    }

    @Bean
    public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();

        tomcat.addContextCustomizers(context -> {
            // HttpOnly 활성화
            context.setUseHttpOnly(true);

            // Secure 플래그 설정
            boolean isSecure = "https".equals(System.getenv("SECURE_ENV")); // 환경 변수로 제어
            if (isSecure) {
                System.setProperty("server.servlet.session.cookie.secure", "true");
            } else {
                System.setProperty("server.servlet.session.cookie.secure", "false");
            }
        });

        return tomcat;
    }

}
