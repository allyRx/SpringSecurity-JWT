package com.vasia.authenticationwithspring.security.repository;

import com.vasia.authenticationwithspring.security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser ,Long> {
    AppUser findAppUserByUsername(String username);
}
