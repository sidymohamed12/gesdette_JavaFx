package com.dette.services;

import java.util.List;

import com.dette.entities.Client;
import com.dette.entities.Dette;
import com.dette.repository.implement.DetteRepository;
import com.dette.services.servicespe.IDetteService;

public class DetteService implements IDetteService {

    private DetteRepository detteRepository;

    public DetteService(DetteRepository detteRepository) {
        this.detteRepository = detteRepository;
    }

    @Override
    public void create(Dette value) {
        value.onPrePersist();
        detteRepository.insert(value);
    }

    @Override
    public List<Dette> findAll() {
        return detteRepository.selectAll();
    }

    @Override
    public Dette getBy(String name) {
        return detteRepository.selectBy(name);
    }

    @Override
    public int count() {
        return detteRepository.count();
    }

    @Override
    public void modifier(Dette dette) {
        dette.onPreUpdated();
        detteRepository.update(dette);
    }

    @Override
    public Dette getById(int id) {
        return detteRepository.selectById(id);
    }

    @Override
    public List<Dette> detteOfClient(Client client) {
        return detteRepository.detteOfClient(client);
    }

}
