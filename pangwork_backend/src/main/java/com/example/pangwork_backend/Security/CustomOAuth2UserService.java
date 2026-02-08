package com.example.pangwork_backend.Security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if ("naver".equals(registrationId)) {
            Map<String, Object> response = getNaverResponse(oauth2User);
            return new DefaultOAuth2User(
                    oauth2User.getAuthorities().isEmpty()
                            ? java.util.List.of(new SimpleGrantedAuthority("ROLE_USER"))
                            : oauth2User.getAuthorities(),
                    response,
                    "id"
            );
        }

        return oauth2User;
    }

    private Map<String, Object> getNaverResponse(OAuth2User oauth2User) {
        Object responseObj = oauth2User.getAttributes().get("response");
        if (responseObj instanceof Map<?, ?> responseMap) {
            Map<String, Object> result = new HashMap<>();
            responseMap.forEach((key, value) -> result.put(String.valueOf(key), value));
            return result;
        }

        return new HashMap<>();
    }
}
