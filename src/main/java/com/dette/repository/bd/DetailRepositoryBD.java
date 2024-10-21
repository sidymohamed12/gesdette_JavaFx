package com.dette.repository.bd;

import com.dette.core.database.implement.RepositoryBDImpl;
import com.dette.entities.Detail;
import com.dette.repository.implement.DetailRepository;

public class DetailRepositoryBD extends RepositoryBDImpl<Detail> implements DetailRepository {

    public DetailRepositoryBD(ArticleRepositoryBD articleRepositoryBD, DetteRepositoryBD detteRepositoryBD) {
        super(null, null, articleRepositoryBD, detteRepositoryBD);
        clazz = Detail.class;
        tableName = "detaildette";
        colomnSelectBy = null;
        colones = new String[] { "qteVendu", "montantVendu", "articleId", "detteId", "createdAt", "updatedAt" };
    }

    public DetailRepositoryBD() {
        super(null, null,new ArticleRepositoryBD(), null);
        clazz = Detail.class;
        tableName = "detaildette";
        colomnSelectBy = null;
        colones = new String[] { "qteVendu", "montantVendu", "articleId", "detteId", "createdAt", "updatedAt" };
    }
    
}
