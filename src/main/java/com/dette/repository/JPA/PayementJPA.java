package com.dette.repository.JPA;

import java.util.List;

import javax.persistence.NoResultException;

import com.dette.core.database.implement.RepositoryJpaImpl;
import com.dette.entities.Dette;
import com.dette.entities.Payement;
import com.dette.repository.implement.PayementRepository;

public class PayementJPA extends RepositoryJpaImpl<Payement> implements PayementRepository{
    public PayementJPA(){
         type = Payement.class;
        coloneSelectBy = null;
    }

    @Override
    public List<Payement> payementsDette(Dette dette) {
        try {
            String sql = String.format("SELECT e FROM %s e WHERE e.%s = :iddette", type.getSimpleName(), "detteId");
            return em.createQuery(sql, type)
                    .setParameter("iddette", dette.getId())
                    .getResultList();
            } catch (NoResultException e) {
                return null; 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
