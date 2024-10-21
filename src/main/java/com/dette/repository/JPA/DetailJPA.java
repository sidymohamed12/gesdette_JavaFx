package com.dette.repository.JPA;

import com.dette.core.database.implement.RepositoryJpaImpl;
import com.dette.entities.Detail;
import com.dette.repository.implement.DetailRepository;

public class DetailJPA extends RepositoryJpaImpl<Detail> implements DetailRepository{
    public DetailJPA() {
        type = Detail.class;
        coloneSelectBy = null;
    }
}
