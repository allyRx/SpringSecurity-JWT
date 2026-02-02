package com.vasia.authenticationwithspring.repository;

import com.vasia.authenticationwithspring.entity.RoleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleUser ,Long> {
    Optional<RoleUser> findByRole(String role);
}
