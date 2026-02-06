package com.example.pangwork_backend.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pangwork_backend.DTO.Users;
import com.example.pangwork_backend.Mapper.UserMapper;
import com.example.pangwork_backend.Service.UserService;

import ch.qos.logback.core.util.StringUtil;

@Service
public class UserServiceImpl implements UserService{
  
  private final UserMapper userMapper;

  public UserServiceImpl (UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  public List<Users> getUsers(){
      return userMapper.selectUsers();
    }

  @Transactional
    public Users getUser(Users user) {
        // 1. 유저 조회
        Users selectedUser = userMapper.selectUser(user);
    
        // 2. 유저가 조회된 경우
        if (selectedUser != null) {
            if (StringUtil.isNullOrEmpty(selectedUser.getUserId())) {
                return new Users();
            }
            // 마지막 로그인 날짜 업데이트
            userMapper.updateLoginDate(selectedUser);
            return selectedUser;
        }
    
        // 3. 유저가 조회 안 된 경우 인서트
        int isInsert = userMapper.insertUser(user);
        return isInsert != 0 ? user : new Users();
    }
}
