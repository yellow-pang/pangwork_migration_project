package com.example.pangwork_backend.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.pangwork_backend.DTO.Users;

@Mapper
public interface UserMapper {
    public List<Users> selectUsers(

    );

    public Users selectUser(
        Users user
    );

    public int insertUser(
        Users user
    );

    public int updateLoginDate(
        Users user
    );
}
