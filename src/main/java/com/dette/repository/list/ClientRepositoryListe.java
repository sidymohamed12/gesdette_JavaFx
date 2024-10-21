package com.dette.repository.list;

import com.dette.core.database.implement.RepositoryListeImplement;
import com.dette.entities.Client;
import com.dette.repository.implement.ClientRepository;

public class ClientRepositoryListe extends RepositoryListeImplement<Client> implements ClientRepository {

    public Client selectBy(String name) {
        return listes.stream()
                .filter(client -> client.getTelephone().compareTo(name) == 0)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(Client client) {
        Client cl = listes.stream()
                .filter(value -> value.getTelephone().compareTo(client.getTelephone()) == 0)
                .findFirst()
                .orElse(null);
        if (cl != null) {
            cl.setSurnom(client.getSurnom());
            cl.setTelephone(client.getTelephone());
            cl.setAdresse(client.getAdresse());
            cl.setUser(client.getUser());
        }
    }

    @Override
    public Client selectById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

}
