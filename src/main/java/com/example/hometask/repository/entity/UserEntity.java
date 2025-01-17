package com.example.hometask.repository.entity;

import jakarta.persistence.*;

import javax.management.relation.Role;

@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String password;
}
