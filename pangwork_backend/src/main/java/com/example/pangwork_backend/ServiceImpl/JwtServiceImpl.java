package com.example.pangwork_backend.ServiceImpl;

import java.util.Date;

import javax.crypto.SecretKey;

import com.example.pangwork_backend.Service.JwtService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtServiceImpl implements JwtService{

  private final String secretKey = "secret1234567843243243243243243243243243243243290"; // 실제로는 더 길고 안전하게
  SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

  @Override
  public String createToken(String userId) {
    return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1시간
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

  @Override
  public String getUserIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
  
}
