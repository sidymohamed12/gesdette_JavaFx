package com.dette.core;

import java.util.List;

public interface Repository<T> {
    void insert(T value);

    List<T> selectAll();

    int count();

}
