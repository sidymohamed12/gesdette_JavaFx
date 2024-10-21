package com.dette.repository.bd;

import com.dette.core.database.implement.RepositoryBDImpl;
import com.dette.entities.Client;
import com.dette.repository.implement.ClientRepository;

public class ClientRepositoryBD extends RepositoryBDImpl<Client> implements ClientRepository {


    public ClientRepositoryBD(UserRepositoryBD userRepository) {
        super(userRepository, null, null, null);
        clazz = Client.class;
        tableName = "client";
        colomnSelectBy = "telephone";
        colones = new String[] { "surnom", "telephone", "adresse", "userId", "createdAt", "updatedAt" };
    }

    public ClientRepositoryBD() {
        super(new UserRepositoryBD(), null, null, null);
        clazz = Client.class;
        tableName = "client";
        colomnSelectBy = "telephone";
        colones = new String[] { "surnom", "telephone", "adresse", "userId", "createdAt", "updatedAt" };
    }

}
