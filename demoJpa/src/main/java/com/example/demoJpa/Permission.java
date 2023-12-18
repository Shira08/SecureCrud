package com.example.demoJpa;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    MANAGER_READ("management:read"),
    MANAGER_DELETE("management:delete"),
    USER_READ("admin:read"),

    ;


    private final String permission;
    public String getPermission() {
        return permission;
    }
    Permission(String permission) {
        this.permission = permission;
    }
}
