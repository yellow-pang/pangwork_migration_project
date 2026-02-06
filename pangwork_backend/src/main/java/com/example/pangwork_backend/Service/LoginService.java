package com.example.pangwork_backend.Service;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Map;

import com.example.pangwork_backend.DTO.NaverLoginResponse;
import com.example.pangwork_backend.DTO.NaverToken;

import jakarta.servlet.http.HttpServletRequest;

public interface LoginService {
    public String getNaverAuthorizeUrl(HttpServletRequest request) throws URISyntaxException, MalformedURLException;

    public NaverToken getNaverTokenUrl(Map<String, String> resValue) throws URISyntaxException, MalformedURLException, UnsupportedEncodingException;

    public NaverLoginResponse getNaverUserByToken(NaverToken responseToken);
}
