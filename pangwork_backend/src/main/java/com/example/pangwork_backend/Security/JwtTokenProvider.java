package com.example.pangwork_backend.Security;

import java.util.Collections;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * JWT 토큰을 생성하고 검증하는 컴포넌트트
 *
 * 주요 기능:
 * - 토큰 생성
 * - 토큰 유효성 검증
 * - 토큰에서 사용자 ID 추출
 */
@Component
public class JwtTokenProvider {
    private final String secretKey = "9da2b9e39e8d3ad162c41fded059796c4b2d6a633b2b040faa6e378cad20c7157f4d8ce44ca2c4e28b1e958840fee2017c733a708437b921c0e3ac4b293957b2"; // 키
    SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes()); // 암호화된 키를 생성
    /**
     * jwt 키를 생성
     * @param userId
     * @return 생성된 jwt키
     */
    public String createToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1시간
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰이 유효한지 검증하는 메서드
     * @param token
     * @return 맞으면 true, 틀리면 false
     */
     public boolean validateToken(String token) {
        try {
            // 서명을 검증하고, 파싱이 성공하면 유효한 토큰
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰이 만료되었거나, 변조되었거나, 잘못된 경우 예외 발생
            return false;
        }
    }

    /**
     * 토큰을 기반으로 Authentication 객체를 생성하는 메서드
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {
        // 토큰을 파싱해서 Claims(토큰 안에 담긴 데이터)를 꺼냄
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Claims 안에서 subject(일반적으로 사용자 ID)를 꺼냄
        String username = claims.getSubject();

        // 스프링 시큐리티 User 객체 생성 (권한은 비워둠)
        User user = new User(username, "", Collections.emptyList());

        // UsernamePasswordAuthenticationToken 객체를 반환 (인증된 사용자 정보)
        return new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
    }

    /**
     * SecurityContextHolder에 Authentication을 저장하는 메서드
     * @param token
     */
    // SecurityContextHolder에 Authentication을 저장하는 메서드
    public void setAuthentication(String token) {
        Authentication authentication = getAuthentication(token);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
