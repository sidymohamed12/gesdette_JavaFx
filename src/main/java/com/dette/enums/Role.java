package com.dette.enums;

public enum Role {
    admin(1), 
    boutiquier(2), 
    client(3);

    private final int id;

    Role(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Role getRole(String value) {
        return java.util.Arrays.stream(Role.values())
                .filter(role -> role.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }

    public static Role getRoleById(int id) {
        return java.util.Arrays.stream(Role.values())
                .filter(role -> role.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
