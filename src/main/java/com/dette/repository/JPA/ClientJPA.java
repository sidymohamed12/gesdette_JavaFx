package com.dette.repository.JPA;

import com.dette.core.database.implement.RepositoryJpaImpl;
import com.dette.entities.Client;
import com.dette.repository.implement.ClientRepository;

public class ClientJPA extends RepositoryJpaImpl<Client> implements ClientRepository {

    public ClientJPA() {
        type = Client.class;
        coloneSelectBy = "telephone";
    }

    // public List<Dette> getDettesNonSoldeesByClient(int clientId) {

    // String jpql = "SELECT d FROM Dette d " +
    // "LEFT JOIN FETCH d.details dtl " +
    // "JOIN FETCH dtl.article a " +
    // "LEFT JOIN FETCH Payement p ON p.dette.id = d.id " +
    // "WHERE d.client.id = :clientId " +
    // "AND (d.montant - d.montantVerser) != 0";

    // return entityManager.createQuery(jpql, Dette.class)
    // .setParameter("clientId", clientId)
    // .getResultList();
    // }

    // -------------------------------------

    // public List<Dette> getDemandesDetteByEtat(Etat etat) {
    // String jpql = "SELECT d FROM Dette d " +
    // "LEFT JOIN FETCH d.details de " +
    // "LEFT JOIN FETCH de.article a " +
    // "WHERE d.etat = :etat";

    // return entityManager.createQuery(jpql, Dette.class)
    // .setParameter("etat", etat)
    // .getResultList();
    // }

}
