package com.vasia.authenticationwithspring.security.service;

import com.vasia.authenticationwithspring.security.entities.AppRole;
import com.vasia.authenticationwithspring.security.entities.AppUser;
import com.vasia.authenticationwithspring.security.repository.AppRoleRepository;
import com.vasia.authenticationwithspring.security.repository.AppUserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional @AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AppUserRepository appUserRepository;
    private final AppRoleRepository appRoleRepository;

    @Override
    public void addNewUser(AppUser appUser) {
        appUserRepository.save(appUser);
    }

    @Override
    public void addNewRole(AppRole appRole) {
        appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        AppUser appUser = appUserRepository.findAppUserByUsername(username);
        AppRole appRole = appRoleRepository.findByRoleName(roleName);

       appUser.getAppRoles().add(appRole);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findAppUserByUsername(username);
    }

    @Override
    public List<AppUser> findAllUser(){
        return appUserRepository.findAll();
    }
}
