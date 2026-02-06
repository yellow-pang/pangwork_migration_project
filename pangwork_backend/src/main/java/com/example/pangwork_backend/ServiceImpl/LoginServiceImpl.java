package com.example.pangwork_backend.ServiceImpl;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.pangwork_backend.DTO.NaverLoginResponse;
import com.example.pangwork_backend.DTO.NaverToken;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class LoginServiceImpl {
    @Value("${login.naver.clientId}")
    private String clientId;

    @Value("${login.naver.clientSecret}")
    private String clientSecret;

    @Value("${login.naver.baseUrl}")
    private String baseUrl;

    @Value("${login.naver.redirectUrl}")
    private String redirectUrl;

    WebClient webClient = WebClient.builder().build();

    public String getNaverAuthorizeUrl(HttpServletRequest request)
            throws URISyntaxException, MalformedURLException {
        
        // 1. CSRF 방지를 위한 state 생성 및 세션에 저장
        String state = UUID.randomUUID().toString();
        request.getSession().setAttribute("oauth_state", state);

        // 2. URI 생성
        UriComponents uriComponents = UriComponentsBuilder // URI를 조립할 때 사용되는 클래스, 내부적으로 문자열을 안전하게 이스케이프 처리하며 URL을 구성
                .fromUriString(baseUrl + "/" + "authorize") // 요청 url은 "https://nid.naver.com/oauth2.0/authorize"
                .queryParam("response_type", "code") // 인증 과정에 대한 내부 구분값 : api 문서에 따라 code로 전송함
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUrl) // 로그인 처리 후 이동할 url 지정
                .queryParam("state", state) //CSRF 방지를 위해 생성한 토큰 값
                .build();
        // 최종 예시
        // https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=CLIENT_ID&state=STATE_STRING&redirect_uri=CALLBACK_URL
        return uriComponents.toUriString(); // 최종 URL 문자열 반환
    }


    public NaverToken getNaverTokenUrl(Map<String, String> resValue) throws URISyntaxException, MalformedURLException, UnsupportedEncodingException {

        return webClient // webClient는 http 클라이언트(비동기)
            .post() // 메서드는 post로 함
            .uri(baseUrl + "/" + "token") // 이번에는 구분값을 토큰으로 함
            .body(BodyInserters // application/x-www-form-urlencoded 형식의 본문을 생성
                .fromFormData("grant_type", "authorization_code") // 토큰 발급이므로 authorization_code, 갱신 : refresh_token, 삭제 : delete
                .with("client_id", clientId)
                .with("client_secret", clientSecret)
                .with("code", resValue.get("code"))
                .with("state", resValue.get("state")))
            .retrieve() // 응답을 받음 => 내부적으로 http 통신이 이루어짐
            .bodyToMono(NaverToken.class) // 응답 본문을 NaverToken DTO로 매핑함
            .block(); // 비동기 클라를 썼는데 동기로 처리하기 위해 씀
    }

    public NaverLoginResponse getNaverUserByToken(NaverToken responseToken){
        final String profileUri = UriComponentsBuilder //UriComponentsBuilder를 통해 uri 생성
            .fromUriString("https://openapi.naver.com")
            .path("/v1/nid/me")
            .build()
            .encode()
            .toUriString();

        return webClient // 다시 http 요청
            .post() // 메서드는 겟
            .uri(profileUri) // uri는 위에서 설정힘
            .header("Authorization", "Bearer " + responseToken.getAccess_token()) // 요청 헤더에 토큰을 보냄
            .retrieve() // 통신 이루어짐
            .bodyToMono(NaverLoginResponse.class) // 응답은 NaverLoginResponse 객체로 매핑
            .block(); // 비동기 처리
    };
}
