package com.dette.repository.JPA;

import javax.persistence.NoResultException;

import com.dette.core.database.implement.RepositoryJpaImpl;
import com.dette.entities.Article;
import com.dette.entities.Client;
import com.dette.entities.User;
import com.dette.repository.implement.ClientRepository;

public class ClientJPA extends RepositoryJpaImpl<Client> implements ClientRepository {

    public ClientJPA() {
        type = Client.class;
        coloneSelectBy = "telephone";
    }

    @Override
    public Client getClientByUser(User user) {
        try {
            String sql = "SELECT c FROM Client c WHERE c.user.id = :userId";
            return em.createQuery(sql, Client.class)
                    .setParameter("userId", user.getId())
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    // -------------------------------------

}
