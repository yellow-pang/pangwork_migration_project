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

    @Override
    public List<Users> getUsers(){
      return userMapper.selectUsers();
    }

    @Override
    @Transactional
    public Users getUser(Users user) {
      Users selectedUser = userMapper.selectUser(user);

      if (selectedUser == null || StringUtil.isNullOrEmpty(selectedUser.getUserId())) {
        return null;
      }

      userMapper.updateLoginDate(selectedUser);
      return selectedUser;
    }

    @Override
    @Transactional
    public Users registerUser(Users user) {
      Users selectedUser = userMapper.selectUser(user);
      if (selectedUser != null && !StringUtil.isNullOrEmpty(selectedUser.getUserId())) {
        return selectedUser;
      }

      int isInsert = userMapper.insertUser(user);
      return isInsert != 0 ? user : null;
    }
}
