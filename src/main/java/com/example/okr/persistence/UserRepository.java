package com.example.okr.persistence;

import com.example.okr.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User,Long> {
    public UserDetails findByUsername(String username);

    public User findByUsernameAndPassword(String username, String password);
}
