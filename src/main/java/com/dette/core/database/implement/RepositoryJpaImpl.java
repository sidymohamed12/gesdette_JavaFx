package com.dette.core.database.implement;

import java.util.*;

import com.dette.core.database.RepositoryJPA;
import com.dette.core.services.YamlService;
import com.dette.core.services.YamlServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

public class RepositoryJpaImpl<T> implements RepositoryJPA<T> {

    protected EntityManager em;
    protected EntityManagerFactory emFactory;
    protected Class<T> type;
    protected String coloneSelectBy;
    YamlService yamlService;

    public RepositoryJpaImpl() {
        yamlService = new YamlServiceImpl();
        Map<String, Object> mapYaml = yamlService.loadYaml();
        emFactory = Persistence.createEntityManagerFactory(mapYaml.get("persistence").toString());

        if (em == null) {
            em = emFactory.createEntityManager();
        }
    }

    @Override
    public void insert(T value) {
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            em.persist(value); 
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Override
    public List<T> selectAll() {
        String sql = String.format("SELECT t FROM %s t", type.getSimpleName());
        return em.createQuery(sql, type).getResultList();
    }

    @Override
    public T selectById(int id) {
        return em.find(type, id);
    }

    @Override
    public void update(T value) {
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            em.merge(value);  // Merge pour mettre à jour les entités détachées
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Override
    public int count() {
        String sql = String.format("SELECT MAX(e.id) FROM %s e", type.getSimpleName());
        Integer maxId = em.createQuery(sql, Integer.class).getSingleResult();
        return maxId != null ? maxId : 0;
    }

    @Override
    public T selectBy(String name) {
        try {
            String sql = String.format("SELECT e FROM %s e WHERE e.%s = :value", type.getSimpleName(), coloneSelectBy);

            T result = em.createQuery(sql, type)
                    .setParameter("value", name)
                    .getSingleResult();

            return result;
            } catch (NoResultException e) {
                return null; 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}


