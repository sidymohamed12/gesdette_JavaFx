package com.dette.core.database;

import com.dette.core.Repository;

public interface RepositoryListe<T> extends Repository<T> {
    void setId(Object obj, int id);

}
