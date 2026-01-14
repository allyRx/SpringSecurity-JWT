package com.vasia.authenticationwithspring.security.service;

import com.vasia.authenticationwithspring.security.entities.AppRole;
import com.vasia.authenticationwithspring.security.entities.AppUser;

import java.util.List;

public interface AccountService  {
    void addNewUser(AppUser appUser);
    void addNewRole(AppRole appRole);
    void addRoleToUser(String username, String roleName);
    AppUser loadUserByUsername(String username);
   List<AppUser> findAllUser();
}
