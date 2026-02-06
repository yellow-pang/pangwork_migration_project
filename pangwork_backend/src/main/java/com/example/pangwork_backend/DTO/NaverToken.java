package com.example.pangwork_backend.DTO;

import lombok.Data;

@Data
public class NaverToken {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private String expires_in;
}
