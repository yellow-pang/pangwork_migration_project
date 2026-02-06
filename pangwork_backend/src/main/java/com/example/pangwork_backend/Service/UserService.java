package com.example.pangwork_backend.Service;

import java.util.List;

import com.example.pangwork_backend.DTO.Users;

public interface UserService {
    public List<Users> getUsers();
    
    public Users getUser(Users user);
}
