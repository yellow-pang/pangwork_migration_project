package com.example.pangwork_backend.DTO;

import lombok.Data;

@Data
public class NaverLoginResponse {
    private String resultcode;
    private String message;
    private NaverLoginProfile response;
}
