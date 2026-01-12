package com.example.okr.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DtoUserLogin {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
