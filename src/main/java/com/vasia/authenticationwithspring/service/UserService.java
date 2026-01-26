package com.vasia.authenticationwithspring.service;

import com.vasia.authenticationwithspring.entity.User;
import com.vasia.authenticationwithspring.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;


    public List<User> getAllUser(){
        return userRepository.findAll();
    }
}
