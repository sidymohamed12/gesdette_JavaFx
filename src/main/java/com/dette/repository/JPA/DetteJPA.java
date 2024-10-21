package com.dette.repository.JPA;

import com.dette.core.database.implement.RepositoryJpaImpl;
import com.dette.entities.Dette;
import com.dette.repository.implement.DetteRepository;

public class DetteJPA extends RepositoryJpaImpl<Dette> implements DetteRepository {
    public DetteJPA(){
         type = Dette.class;
        coloneSelectBy = "libelle";
    }
}
