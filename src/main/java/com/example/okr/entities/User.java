package com.example.okr.entities;


import com.example.okr.dto.user.DtoUser;
import com.example.okr.dto.user.DtoUserLogin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    private String token;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private List<KeyResult> keyResults = new ArrayList<>();

    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = new Date();
    }

    public User(DtoUser dtoUser) {
        this.user_id = dtoUser.getUser_id();
        this.username = dtoUser.getUsername();
        this.password = dtoUser.getPassword();
        this.keyResults = dtoUser.getKeyResultList();
    }
    public User(DtoUserLogin dtoUserLogin) {
        this.username = dtoUserLogin.getUsername();
        this.password = dtoUserLogin.getPassword();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
