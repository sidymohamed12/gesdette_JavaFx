package com.dette.repository.implement;

import com.dette.core.Repository;
import com.dette.entities.Client;
import com.dette.entities.User;

public interface ClientRepository extends Repository<Client> {
    Client selectBy(String name);

    Client selectById(int id);

    void update(Client client);

    Client getClientByUser(User user);
}
