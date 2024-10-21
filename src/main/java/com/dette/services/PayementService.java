package com.dette.services;

import java.util.List;

import com.dette.entities.Dette;
import com.dette.entities.Payement;
import com.dette.repository.implement.PayementRepository;
import com.dette.services.servicespe.IPayementService;

public class PayementService implements IPayementService {

    private PayementRepository payementRepository;

    public PayementService(PayementRepository payementRepository) {
        this.payementRepository = payementRepository;
    }

    @Override
    public void create(Payement value) {
        value.onPrePersist();
        payementRepository.insert(value);
    }

    @Override
    public List<Payement> findAll() {
        return payementRepository.selectAll();
    }

    @Override
    public Payement getBy(String name) {
        return payementRepository.selectBy(name);
    }

    @Override
    public int count() {
        return payementRepository.count();
    }

    @Override
    public void modifier(Payement payement) {
        payement.onPrePersist();
        payementRepository.update(payement);
    }

    @Override
    public Payement getById(int id) {
        return payementRepository.selectById(id);
    }

    @Override
    public List<Payement> getPayementsDette(Dette dette) {
        return payementRepository.payementsDette(dette);
    }

}
