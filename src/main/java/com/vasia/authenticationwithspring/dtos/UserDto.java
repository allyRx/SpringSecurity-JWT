package com.vasia.authenticationwithspring.dtos;

import java.util.List;

public record UserDto(
        Long id,
        String name,
        String email,
        List<String> roles){}


