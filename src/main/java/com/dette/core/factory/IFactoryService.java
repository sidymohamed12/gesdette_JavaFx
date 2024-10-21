package com.dette.core.factory;

import com.dette.core.Service;

public interface IFactoryService<T> {
    Service<T> createService();
}
