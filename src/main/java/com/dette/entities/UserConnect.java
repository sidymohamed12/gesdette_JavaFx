package com.dette.entities;

public class UserConnect {
    public static User userConnecte;

    public static User getUserConnecte() {
        return userConnecte;
    }

    public static void setUserConnecte(User userConnecte) {
        UserConnect.userConnecte = userConnecte;
    }
}
