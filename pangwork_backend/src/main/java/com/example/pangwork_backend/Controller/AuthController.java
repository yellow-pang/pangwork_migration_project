package com.example.pangwork_backend.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pangwork_backend.DTO.Users;
import com.example.pangwork_backend.Security.JwtTokenProvider;
import com.example.pangwork_backend.Service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/auth/consent")
    public ResponseEntity<Map<String, Object>> consent(
            @RequestBody Users user,
            HttpServletResponse response
    ) {
        Map<String, Object> result = new HashMap<>();
        if (user == null || user.getUserId() == null || user.getUserId().isBlank()) {
            result.put("status", "error");
            result.put("message", "Missing userId");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        Users created = userService.registerUser(user);
        if (created == null || created.getUserId() == null || created.getUserId().isBlank()) {
            result.put("status", "error");
            result.put("message", "User registration failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }

        String jwt = jwtTokenProvider.createToken(created.getUserId());
        Cookie cookie = new Cookie("ACCESS_TOKEN", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);

        result.put("status", "ok");
        result.put("userId", created.getUserId());
        result.put("nickName", created.getNickName());
        return ResponseEntity.ok(result);
    }
}
