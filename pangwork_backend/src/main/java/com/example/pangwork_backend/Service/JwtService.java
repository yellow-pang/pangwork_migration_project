package com.example.pangwork_backend.Service;

public interface JwtService {
    public String createToken(String userId);

    public String getUserIdFromToken(String token);
}
