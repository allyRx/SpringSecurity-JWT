package com.vasia.authenticationwithspring.service;


import com.vasia.authenticationwithspring.dtos.LoginUserDto;
import com.vasia.authenticationwithspring.dtos.RegisterUserDto;
import com.vasia.authenticationwithspring.entity.RoleUser;
import com.vasia.authenticationwithspring.entity.User;
import com.vasia.authenticationwithspring.repository.RoleRepository;
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
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public User SignUp(RegisterUserDto userInput){
        User newUser = new User();
        newUser.setFullName(userInput.getFullName());
        newUser.setEmail(userInput.getEmail());
        newUser.setPassword(passwordEncoder.encode(userInput.getPassword()));

        // Assign default USER role
        RoleUser userRole = roleRepository.findByRole("USER")
                .orElseGet(() -> {
                    RoleUser newRole = new RoleUser();
                    newRole.setRole("USER");
                    return roleRepository.save(newRole);
                });
        newUser.getRoles().add(userRole);

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
