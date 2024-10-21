package com.dette.enums;

public enum Etat {
    encours(1), annuler(2), accepter(3);
    private final int id;

    Etat (int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Etat getEtat(String value) {
        return java.util.Arrays.stream(Etat.values())
                .filter(etat -> etat.name().compareTo(value) == 0)
                .findFirst()
                .orElse(null);
    }

    public static Etat getEtatById(int id) {
        return java.util.Arrays.stream(Etat.values())
                .filter(etat -> etat.getId() == (id))
                .findFirst()
                .orElse(null);
    }
}
