package com.example.pangwork_backend.Security;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.pangwork_backend.DTO.Users;
import com.example.pangwork_backend.Service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Value("${app.frontend.redirect-url:/}")
    private String frontendRedirectUrl;

    @Value("${app.frontend.consent-url:/consent}")
    private String frontendConsentUrl;

    public OAuth2AuthenticationSuccessHandler(
            JwtTokenProvider jwtTokenProvider,
            UserService userService
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String naverUserId = oauth2User.getAttribute("id");
        String naverNickname = oauth2User.getAttribute("nickname");

        if (naverUserId == null || naverUserId.isBlank()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing user id from OAuth2 provider");
            return;
        }

        Users tempUser = new Users();
        tempUser.setUserId(naverUserId);
        tempUser.setNickName(naverNickname);

        Users proceedUser = userService.getUser(tempUser);
        if (proceedUser == null || proceedUser.getUserId() == null || proceedUser.getUserId().isBlank()) {
            String encodedId = URLEncoder.encode(naverUserId, StandardCharsets.UTF_8);
            String encodedNickname = URLEncoder.encode(naverNickname == null ? "" : naverNickname, StandardCharsets.UTF_8);
            String redirectUrl = frontendConsentUrl + "?userId=" + encodedId + "&nickName=" + encodedNickname;
            response.sendRedirect(redirectUrl);
            return;
        }

        String jwt = jwtTokenProvider.createToken(proceedUser.getUserId());
        Cookie cookie = new Cookie("ACCESS_TOKEN", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);

        response.sendRedirect(frontendRedirectUrl);
    }
}
