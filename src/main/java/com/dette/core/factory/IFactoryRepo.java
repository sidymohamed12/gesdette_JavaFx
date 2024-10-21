package com.dette.core.factory;

import com.dette.core.Repository;

public interface IFactoryRepo<T> {
    Repository<T> createRepository();
}
