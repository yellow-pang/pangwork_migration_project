package com.example.pangwork_backend.Controller;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pangwork_backend.DTO.NaverLoginProfile;
import com.example.pangwork_backend.DTO.NaverLoginResponse;
import com.example.pangwork_backend.DTO.NaverToken;
import com.example.pangwork_backend.DTO.Users;
import com.example.pangwork_backend.Security.JwtTokenProvider;
import com.example.pangwork_backend.Service.LoginService;
import com.example.pangwork_backend.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;

@Controller
public class LoginController {
  
  private final LoginService loginService;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserService userService;
  

  public LoginController(LoginService loginService, JwtTokenProvider jwtTokenProvider, UserService userService) {
    this.loginService = loginService;
    this.jwtTokenProvider = jwtTokenProvider;
    this.userService = userService;
  }
  
  @RequestMapping(value = "/naverlogin", method = RequestMethod.GET)
    public void naverLogin(HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, UnsupportedEncodingException, URISyntaxException {
        String url = loginService.getNaverAuthorizeUrl(request);
        System.out.println(url);
        try {
            response.sendRedirect(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/navercallback", method = RequestMethod.GET)
    // public Map<String, Object> callBack(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> resValue) throws Exception {
    public void callBack(
            HttpServletRequest request
            ,HttpServletResponse response
            ,@RequestParam Map<String, String> resValue
        ) throws Exception {
            
        Map<String, Object> result = new HashMap<>();
        // 1. 에러 핸들링
        if (resValue.get("error") != null) {
            result.put("status", "900");
        }

        // 2. CSRF 방지 - state 검증
        String sessionState = (String) request.getSession().getAttribute("oauth_state");
        if (sessionState == null || !sessionState.equals(resValue.get("state"))) {
            throw new SecurityException("State mismatch: possible CSRF");
        }

        // 3. 액세스 토큰 요청
        NaverToken responseToken = loginService.getNaverTokenUrl(resValue);
        System.out.println("확인 2 : 토큰까지 발급" + responseToken);

        // 4. 사용자 정보 요청
        NaverLoginResponse naverUser = loginService.getNaverUserByToken(responseToken);

        // 5. 사용자 정보 출력
        NaverLoginProfile naverLoginProfile = naverUser.getResponse(); // 응답에서 사용자 정보 세팅
        String naverUserId = naverLoginProfile.getId(); // 네이버에서 반환된 응답에서 아이디 추출
        String naverNickname = naverLoginProfile.getNickname(); // 네이버에서 반환된 응답에서 닉네임
        Users tempUser = new Users(); // 서비스에 보낼 USers 객체 생성
        tempUser.setUserId(naverUserId); 
        tempUser.setNickName(naverNickname);

        // DB 작업 처리
        Users proceceedUser = userService.getUser(tempUser);

        String userId = proceceedUser.getUserId();
        // 6. JWT 생성
        String jwt = jwtTokenProvider.createToken(userId); // JwtService는 직접 만든 서비스 

        // 7. JWT를 HttpOnly 쿠키로 설정
        Cookie cookie = new Cookie("ACCESS_TOKEN", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // HTTPS 환경일 경우 true
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60); // 1시간 유효
        response.addCookie(cookie);

        // 8. 사용자 정보를 JSON 응답으로 반환
        String dummyUserId = UUID.randomUUID().toString();
        String nickName = proceceedUser.getNickName();
        HttpSession session = request.getSession();
        session.setAttribute("nickName", nickName);
        session.setAttribute("userId", dummyUserId);
        // return "main";
        // response.sendRedirect("http://localhost:8076");
        response.sendRedirect("/");
    }  
  

}
