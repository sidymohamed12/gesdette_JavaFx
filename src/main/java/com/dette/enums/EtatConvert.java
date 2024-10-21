package com.dette.enums;

import javax.persistence.AttributeConverter;

public class EtatConvert implements AttributeConverter<Etat, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Etat etat) {
        // Retourne l'ID de l'énumération (commence par 1)
        return etat != null ? etat.getId() : null;
    }

    @Override
    public Etat convertToEntityAttribute(Integer id) {
        // Récupère le rôle basé sur l'ID
        return Etat.getEtatById(id);
    }
}
