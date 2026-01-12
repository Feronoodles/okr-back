package com.example.okr.dto.user;


import com.example.okr.entities.KeyResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DtoUser {
    @NotNull
    private Long user_id;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String token;
    @NotBlank
    private List<KeyResult> keyResultList;
}
