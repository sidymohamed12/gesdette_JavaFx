package com.dette.core;

import java.util.List;

public interface Service<T> {
    void create(T value);

    List<T> findAll();

    T getBy(String name);

    int count();
}
