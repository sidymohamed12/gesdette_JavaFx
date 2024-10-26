package com.dette.services;

import java.util.List;

import com.dette.entities.Client;
import com.dette.entities.User;
import com.dette.repository.implement.ClientRepository;
import com.dette.services.servicespe.IClientService;

public class ClientService implements IClientService {
    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void create(Client cl) {
        cl.onPrePersist();
        clientRepository.insert(cl);
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.selectAll();
    }

    @Override
    public Client getBy(String name) {
        return clientRepository.selectBy(name);
    }

    @Override
    public int count() {
        return clientRepository.count();
    }

    @Override
    public void modifier(Client client) {
        client.onPreUpdated();
        clientRepository.update(client);
    }

    @Override
    public Client getClientByUser(User user) {
        return clientRepository.getClientByUser(user);
    }
}
