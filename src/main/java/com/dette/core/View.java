package com.dette.core;

import java.util.List;

public interface View<T> {
    T saisie();

    void afficher(List<T> listes);

    T getBy();

    boolean askToContinue();
}
