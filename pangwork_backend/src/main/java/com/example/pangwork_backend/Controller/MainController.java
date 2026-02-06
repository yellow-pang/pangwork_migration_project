package com.example.pangwork_backend.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 메인 페이지 컨트롤러
 * Vue SPA를 위한 라우팅
 */
@Controller
@RequestMapping("/")
public class MainController {

    /**
     * 메인 페이지 (Vue SPA)
     * 모든 라우트는 Vue Router에서 처리
     */
    @GetMapping("")
    public String getMain() {
        return "main";
    }

    /**
     * 로그인 페이지
     * Vue의 /login 라우트로 전달
     */
    @GetMapping("/login")
    public String getLogin() {
        return "main"; // Vue SPA의 index.html을 반환 (Thymeleaf main.html이 SPA 역할)
    }
}
