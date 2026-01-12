package com.example.okr.controller;

import com.example.okr.dto.user.DtoUser;
import com.example.okr.dto.user.DtoUserLogin;
import com.example.okr.dto.user.DtoUserView;
import com.example.okr.entities.User;
import com.example.okr.infra.security.DataJWT;
import com.example.okr.infra.security.TokenService;
import com.example.okr.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@CrossOrigin(origins = "*") // Permite peticiones de cualquier origen
public class AuthController {
    public AuthenticationManager authenticationManager;
    private TokenService tokenService;

    private IUserService userService;
    private TokenService decodeToken;

    @Operation(summary = "User authentication", responses = {
            @ApiResponse(responseCode = "200", description = "Successful authentication", content = @Content(mediaType = "application/json",schema = @Schema(implementation = DtoUserLogin.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")})
    @PostMapping("/login")
    public ResponseEntity userAuth(@RequestBody @Valid DtoUserLogin dtoUserLogin) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(dtoUserLogin.getUsername(),dtoUserLogin.getPassword());
        authenticationManager.authenticate(authToken);

        var userAuth = authenticationManager.authenticate(authToken);

        var JWTToken = tokenService.generateToken((User) userAuth.getPrincipal());

        String usuarioId = decodeToken.decodeToken(JWTToken)[1];

        User user = userService.findById(Long.parseLong(usuarioId));

        DtoUser mUsuarioActualizar = new DtoUser(user.getUser_id(),user.getUsername(),user.getPassword(),JWTToken,user.getKeyResults());

        this.userService.updateUserToken(mUsuarioActualizar);
        return ResponseEntity.ok(new DataJWT(JWTToken));
    }
    @Operation(summary = "Get user information", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")})
    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/get_user")
    public ResponseEntity<DtoUserView> getUser(@RequestHeader(value = "Authorization",required = false) String encoding)
    {

        User user = userService.findUser(encoding);
        DtoUserView dtoUserView = DtoUserView.builder()
                                    .user_id(user.getUser_id())
                                    .username(user.getUsername())
                                    .build();
        return ResponseEntity.ok(dtoUserView);
    }
}
