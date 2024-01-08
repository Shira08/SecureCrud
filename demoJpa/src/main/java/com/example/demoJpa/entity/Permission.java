package com.example.demoJpa.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public enum Permission {
    USER_READ("user:read"),
    USER_CREATE("user:create"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete"),
    MATERIAL_READ("material:read"),
    MATERIAL_CREATE("material:create"),
    MATERIAL_UPDATE("material:update"),
    MATERIAL_DELETE("material:delete"),
    EMPLOYEE_READ("employee:read"),
    EMPLOYEE_CREATE("employee:create"),
    EMPLOYEE_UPDATE("employee:update"),
    EMPLOYEE_DELETE("employee:delete");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return permission;
    }

    public String getPermission() {
        return permission;
    }
}

