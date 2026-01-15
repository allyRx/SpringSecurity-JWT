package com.vasia.authenticationwithspring.entity;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Data
@Table(name = "roles")
public class Role {
    private Long id;

    @Enumerated(EnumType.STRING)
    private ERole name;
}
