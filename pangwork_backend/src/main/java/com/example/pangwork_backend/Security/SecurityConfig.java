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
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    // 생성자에서 JwtTokenProvider를 주입받음
    public SecurityConfig(
            JwtTokenProvider jwtTokenProvider,
            CustomOAuth2UserService customOAuth2UserService,
            OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
    }

    // 시큐리티 필터 체인 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                            "/login"
                        ,"/oauth2/**"
                        ,"/login/oauth2/**"
                            ,"/images/**"
                            ,"/css/**"
                            ,"/js/**"
                            ,"/test/**"
                            )
                        .permitAll()
                        .anyRequest().authenticated() // 그 외 요청은 인증 필요
                )
                .oauth2Login(oauth2 -> oauth2
                    .loginPage("/login")
                    .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService)
                    )
                    .successHandler(oAuth2AuthenticationSuccessHandler)
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
