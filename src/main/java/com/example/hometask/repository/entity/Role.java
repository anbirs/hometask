package com.example.hometask.repository.entity;

import java.util.Arrays;

public enum Role {
    ROLE_ADMIN("ADMIN"),
    ROLE_CUSTOMER("CUSTOMER");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Role getByName(String name) {
        return Arrays.stream(Role.values())
                .filter(role -> role.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No role found with name: " + name));
    }
}