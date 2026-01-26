package com.vasia.authenticationwithspring.service;


import com.vasia.authenticationwithspring.dtos.LoginUserDto;
import com.vasia.authenticationwithspring.dtos.RegisterUserDto;
import com.vasia.authenticationwithspring.entity.User;
import com.vasia.authenticationwithspring.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public User SignUp(RegisterUserDto userInput){
        User newUser = new User();
        newUser.setFullName(userInput.getFullName());
        newUser.setEmail(userInput.getEmail());
        newUser.setPassword(passwordEncoder.encode(userInput.getPassword()));

        return userRepository.save(newUser);

    }


    public User authenticate(LoginUserDto loginUserDto){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDto.getEmail(),
                        loginUserDto.getPassword()
                )
        );

        return userRepository.findByEmail(loginUserDto.getEmail()).orElseThrow();
    }

}
