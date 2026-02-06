package com.example.pangwork_backend.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 스프링 시큐리티 설정 파일
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    // 생성자에서 JwtTokenProvider를 주입받음
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 시큐리티 필터 체인 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                            "/login"
                            ,"/naverlogin"
                            ,"/navercallback"
                            ,"/images/**"
                            ,"/css/**"
                            ,"/js/**"
                            ,"/test/**"
                            )
                        .permitAll()
                        .anyRequest().authenticated() // 그 외 요청은 인증 필요
                )
                .formLogin(form -> form
                .loginPage("/login") // 커스텀 로그인 페이지를 "/login"로 사용
                .permitAll()
                )
                // .exceptionHandling(exception -> exception
                //     .authenticationEntryPoint((request, response, authException) ->
                //         response.sendRedirect("/login"))
                //     .accessDeniedHandler((request, response, accessDeniedException) ->
                //         response.sendRedirect("/login"))
                // )
                // UsernamePasswordAuthenticationFilter 앞에 JwtAuthenticationFilter 추가
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
