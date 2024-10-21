package com.dette.repository.bd;

import java.util.List;

import com.dette.core.database.implement.RepositoryBDImpl;
import com.dette.entities.Dette;
import com.dette.entities.Detail;
import com.dette.repository.implement.DetteRepository;

public class DetteRepositoryBD extends RepositoryBDImpl<Dette> implements DetteRepository{

    private DetailRepositoryBD detailRepository = new DetailRepositoryBD();
    
    public DetteRepositoryBD(ClientRepositoryBD clientRepositoryBD) {
        super(null, clientRepositoryBD, null, null);
        clazz = Dette.class;
        tableName = "dette";
        colomnSelectBy = "libelle";
        colones = new String[] { "montant", "montantVerser", "archiver", "date","clientId", "etatId", "createdAt", "updatedAt" };
    }

    public DetteRepositoryBD() {
        super(null, new ClientRepositoryBD(), null, null);
        clazz = Dette.class;
        tableName = "dette";
        colomnSelectBy = "libelle";
        colones = new String[] { "montant", "montantVerser", "archiver", "date", "clientId", "etatId", "createdAt", "updatedAt" };
    }

    @Override
    public void insert(Dette dette) {
        super.insert(dette);
        System.out.println(dette);
        List<Detail> details = dette.getDetails();
        for (Detail detail : details) {
            detail.onPrePersist();
            detailRepository.insert(detail);
        }

    }
}
