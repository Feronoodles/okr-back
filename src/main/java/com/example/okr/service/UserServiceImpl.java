package com.example.okr.service;

import com.example.okr.dto.user.DtoUser;
import com.example.okr.dto.user.DtoUserLogin;
import com.example.okr.entities.User;
import com.example.okr.infra.security.TokenService;
import com.example.okr.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService{
    TokenService tokenService;
    UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User save(DtoUserLogin dtoUserLogin) {
        dtoUserLogin.setPassword(passwordEncoder.encode(dtoUserLogin.getPassword()));
        User user = new User(dtoUserLogin);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUserToken(DtoUser dtoUser) {
        User user = new User(dtoUser);
        return (User)userRepository.save(user);
    }

    @Override
    public User findById(Long user_id) {
        return userRepository.getReferenceById(user_id);
    }

    @Override
    public User findUser(String encode) {

        Long userId = Long.parseLong(tokenService.decodeToken(encode)[1]);

        return userRepository.getReferenceById(userId);
    }
}
