package com.dette.repository.JPA;

import java.util.List;

import javax.persistence.NoResultException;

import com.dette.core.database.implement.RepositoryJpaImpl;
import com.dette.entities.Client;
import com.dette.entities.Dette;
import com.dette.repository.implement.DetteRepository;

public class DetteJPA extends RepositoryJpaImpl<Dette> implements DetteRepository {
    public DetteJPA() {
        type = Dette.class;
        coloneSelectBy = null;
    }

    public List<Dette> detteOfClient(Client client) {
        try {
            String sql = "SELECT d FROM Dette d WHERE d.clientD.id = :clientId";
            List<Dette> result = em.createQuery(sql, type)
                    .setParameter("clientId", client.getId())
                    .getResultList();
            return result;

        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
