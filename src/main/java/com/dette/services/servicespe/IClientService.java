package com.dette.services.servicespe;

import com.dette.core.Service;
import com.dette.entities.Client;

public interface IClientService extends Service<Client> {
    void modifier(Client client);
}
