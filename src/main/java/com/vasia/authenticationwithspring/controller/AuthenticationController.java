package com.vasia.authenticationwithspring.controller;


import com.vasia.authenticationwithspring.dtos.LoginResponse;
import com.vasia.authenticationwithspring.dtos.LoginUserDto;
import com.vasia.authenticationwithspring.dtos.RegisterUserDto;
import com.vasia.authenticationwithspring.entity.User;
import com.vasia.authenticationwithspring.service.AuthService;
import com.vasia.authenticationwithspring.service.JwtService;
import com.vasia.authenticationwithspring.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthService authService;
    private final UserService userService;

    public AuthenticationController(JwtService jwtService, AuthService authService, UserService userService) {
        this.jwtService = jwtService;
        this.authService = authService;
        this.userService = userService;
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

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/user")
    public List<User> getAllUser(){
        return userService.getAllUser();
    }
}
