package com.dette.core.database;

import com.dette.core.Repository;

public interface RepositoryBD<T> extends Repository<T> {

    T selectById(int id);

    T selectBy(String name);

    void update(T value);
}
