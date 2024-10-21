package com.dette.repository.bd;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dette.core.database.implement.RepositoryBDImpl;
import com.dette.entities.Dette;
import com.dette.entities.Payement;
import com.dette.repository.implement.PayementRepository;

public class PayementRepositoryBD  extends RepositoryBDImpl<Payement> implements PayementRepository{

    public PayementRepositoryBD(DetteRepositoryBD detteRepositoryBD) {
        
        super(null, null, null, detteRepositoryBD);
        clazz = Payement.class;
        tableName = "payement";
        colomnSelectBy = null;
        colones = new String[] { "date", "detteId", "montant", "createdAt", "updatedAt" };
    
    }

    @Override
    public List<Payement> payementsDette(Dette dette) {
        List<Payement> payements = new ArrayList<>();
        try {
            String sql = generateSql("SELECT", tableName, colones, "detteId", (int)dette.getId());
            ResultSet res = executeQuery(sql);
            while (res.next()) {
                payements.add(converToObjet(res, Payement.class));
            }
        } catch (SQLException e) {
            System.out.println("Erreur requête : " + e.getMessage());
        }
        return payements;
    }
    

}
