package com.vasia.authenticationwithspring.repository;

import com.vasia.authenticationwithspring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshToken extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findById(Long id);

    void deleteByUser(User user);
}
