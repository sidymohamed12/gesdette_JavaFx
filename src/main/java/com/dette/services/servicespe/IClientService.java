package com.dette.services.servicespe;

import com.dette.core.Service;
import com.dette.entities.Client;
import com.dette.entities.User;

public interface IClientService extends Service<Client> {
    void modifier(Client client);

    Client getClientByUser(User user);
}
