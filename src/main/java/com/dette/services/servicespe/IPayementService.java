package com.dette.services.servicespe;

import java.util.List;

import com.dette.core.Service;
import com.dette.entities.Dette;
import com.dette.entities.Payement;

public interface IPayementService extends Service<Payement> {
    void modifier(Payement payement);
    Payement getById(int id);
    List<Payement> getPayementsDette(Dette dette);

}
