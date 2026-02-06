package com.example.pangwork_backend.Security;


import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private final JwtTokenProvider jwtTokenProvider; // 토큰 검증을 담당할 Provider

    // 생성자에서 JwtTokenProvider를 주입받음
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 요청당 한번 만 실행되는 메서드
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {

    
        // 1. 쿠키에서 access_token이라는 이름의 토큰 꺼내기
        String token = resolveTokenFromCookie(request);
        // System.out.println("[DEBUG] token: " + token); // 디버깅용
        // 2. 토큰이 존재하고 유효하다면
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 3. 토큰을 기반으로 인증(Authentication) 객체를 만들고 SecurityContext에 저장
            jwtTokenProvider.setAuthentication(token);
        }

        // 4. 다음 필터(혹은 컨트롤러)로 요청을 넘김
        filterChain.doFilter(request, response);
    }
    
    private String resolveTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
    
        for (Cookie cookie : request.getCookies()) {
            if ("ACCESS_TOKEN".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
