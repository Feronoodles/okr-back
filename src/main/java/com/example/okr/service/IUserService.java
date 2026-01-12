package com.example.okr.service;

import com.example.okr.dto.user.DtoUser;
import com.example.okr.dto.user.DtoUserLogin;
import com.example.okr.entities.User;

public interface IUserService {
    public User save(DtoUserLogin dtoUserLogin);
    public User updateUserToken(DtoUser dtoUser);
    public User findById(Long user_id);
    public User findUser(String encode);
}
