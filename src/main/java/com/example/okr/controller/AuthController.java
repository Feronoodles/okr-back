package com.example.okr.controller;

import com.example.okr.dto.auth.DtoRefreshTokenRequest;
import com.example.okr.dto.user.DtoUserLogin;
import com.example.okr.dto.user.DtoUserView;
import com.example.okr.entities.User;
import com.example.okr.infra.security.DataJWT;
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
import com.example.okr.service.RefreshTokenService;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@CrossOrigin(origins = {"http://127.0.0.1:5500","https://startling-genie-30d1f7.netlify.app"})// Permite peticiones de cualquier origen
public class AuthController {
    public AuthenticationManager authenticationManager;
    private RefreshTokenService refreshTokenService;

    @Operation(summary = "User authentication", responses = {
            @ApiResponse(responseCode = "200", description = "Successful authentication", content = @Content(mediaType = "application/json",schema = @Schema(implementation = DtoUserLogin.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")})
    @PostMapping("/login")
    public ResponseEntity<DataJWT> userAuth(@RequestBody @Valid DtoUserLogin dtoUserLogin) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(dtoUserLogin.getUsername(),dtoUserLogin.getPassword());
        var userAuth = authenticationManager.authenticate(authToken);

        return ResponseEntity.ok(refreshTokenService.createSession((User) userAuth.getPrincipal()));
    }

    @Operation(summary = "Refresh authenticated session", responses = {
            @ApiResponse(responseCode = "200", description = "Session refreshed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataJWT.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @PostMapping("/refresh")
    public ResponseEntity<DataJWT> refreshSession(@RequestBody @Valid DtoRefreshTokenRequest request) {
        return ResponseEntity.ok(refreshTokenService.refreshSession(request.getRefreshToken()));
    }

    @Operation(summary = "Logout current session", responses = {
            @ApiResponse(responseCode = "204", description = "Successful logout"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @SecurityRequirement(name = "bearer-key")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication, @RequestBody @Valid DtoRefreshTokenRequest request) {
        refreshTokenService.revokeToken(request.getRefreshToken(), (User) authentication.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Logout all sessions", responses = {
            @ApiResponse(responseCode = "204", description = "All sessions revoked"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @SecurityRequirement(name = "bearer-key")
    @PostMapping("/logout-all")
    public ResponseEntity<Void> logoutAll(Authentication authentication) {
        refreshTokenService.revokeAll((User) authentication.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get user information", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")})
    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/get_user")
    public ResponseEntity<DtoUserView> getUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        DtoUserView dtoUserView = DtoUserView.builder()
                                    .user_id(user.getUser_id())
                                    .username(user.getUsername())
                                    .build();
        return ResponseEntity.ok(dtoUserView);
    }
}
