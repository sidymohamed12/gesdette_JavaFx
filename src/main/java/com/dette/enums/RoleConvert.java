package com.dette.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = false) // autoApply = false pour éviter une application globale
public class RoleConvert implements AttributeConverter<Role, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Role role) {
        // Retourne l'ID de l'énumération (commence par 1)
        return role != null ? role.getId() : null;
    }

    @Override
    public Role convertToEntityAttribute(Integer id) {
        // Récupère le rôle basé sur l'ID
        return Role.getRoleById(id);
    }
}
