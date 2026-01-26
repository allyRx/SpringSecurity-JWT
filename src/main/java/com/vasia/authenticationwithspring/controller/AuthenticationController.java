package com.vasia.authenticationwithspring.controller;


import com.vasia.authenticationwithspring.dtos.LoginResponse;
import com.vasia.authenticationwithspring.dtos.LoginUserDto;
import com.vasia.authenticationwithspring.dtos.RefreshRequest;
import com.vasia.authenticationwithspring.dtos.RegisterUserDto;
import com.vasia.authenticationwithspring.entity.RefreshToken;
import com.vasia.authenticationwithspring.entity.User;
import com.vasia.authenticationwithspring.service.AuthService;
import com.vasia.authenticationwithspring.service.JwtService;
import com.vasia.authenticationwithspring.service.RefreshTokenService;
import com.vasia.authenticationwithspring.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthService authService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public AuthenticationController(JwtService jwtService, AuthService authService, UserService userService, RefreshTokenService refreshTokenService) {
        this.jwtService = jwtService;
        this.authService = authService;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }


    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authService.SignUp(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);
        String refreshToken = refreshTokenService.create(authenticatedUser.getId()).getToken();

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        loginResponse.setRefreshToken(refreshToken);

        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/user")
    public List<User> getAllUser(){
        return userService.getAllUser();
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @RequestBody RefreshRequest request) {

        RefreshToken refreshToken = refreshTokenService.verify(request.refreshToken());

        User user = refreshToken.getUser();
        String newAccessToken = jwtService.generateToken(user);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setRefreshToken(refreshToken.getToken());
        loginResponse.setToken(newAccessToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());


        return ResponseEntity.ok(loginResponse);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout( @AuthenticationPrincipal User user) {

        refreshTokenService.deleteByUserId(user.getId());
        return ResponseEntity.ok("Logout OK");
    }


}
