package com.dette.core.factory;

import com.dette.core.View;

public interface IFactoryView<T> {
    View<T> createView();
}
