package com.dette.repository.implement;

import java.util.*;
import com.dette.core.Repository;
import com.dette.entities.Dette;
import com.dette.entities.Payement;

public interface PayementRepository extends Repository<Payement> {
     Payement selectBy(String name);

    Payement selectById(int id);

    void update(Payement payement);

    List<Payement> payementsDette(Dette dette);

}
