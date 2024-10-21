package com.dette.core.database.implement;

import java.util.ArrayList;
import java.util.List;

import com.dette.core.database.RepositoryListe;

import java.lang.reflect.Field;

public class RepositoryListeImplement<T> implements RepositoryListe<T> {

    protected List<T> listes = new ArrayList<>();

    @Override
    public void insert(T value) {
        int id = count() + 1;
        setId(value, id);
        listes.add(value);
    }

    @Override
    public List<T> selectAll() {
        return listes;
    }

    @Override
    public int count() {
        return listes.size();
    }

    @Override
    public void setId(Object obj, int id) {
        try {
            // Obtenir la classe de l'objet
            Class<?> clazz = obj.getClass();

            // Chercher le champ "id"
            Field field = clazz.getDeclaredField("id");

            // Rendre le champ accessible (s'il est privé)
            field.setAccessible(true);

            // Vérifier si le champ "id" a déjà une valeur
            Object currentIdValue = field.get(obj);

            if (currentIdValue != null && (int) currentIdValue != 0) {
                // Si l'ID est déjà défini, ne pas le modifier
                return;
            }

            // Affecter la nouvelle valeur du champ "id"
            field.setInt(obj, id);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }

}
