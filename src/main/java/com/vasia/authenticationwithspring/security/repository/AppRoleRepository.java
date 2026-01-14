package com.vasia.authenticationwithspring.security.repository;

import com.vasia.authenticationwithspring.security.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;



public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
    AppRole findByRoleName(String roleName);
}
