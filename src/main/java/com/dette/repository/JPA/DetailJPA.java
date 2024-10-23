package com.dette.repository.JPA;

import java.util.ArrayList;
import java.util.List;

import com.dette.core.database.implement.RepositoryJpaImpl;
import com.dette.entities.Article;
import com.dette.entities.Detail;
import com.dette.entities.Dette;
import com.dette.repository.implement.DetailRepository;

public class DetailJPA extends RepositoryJpaImpl<Detail> implements DetailRepository {
    public DetailJPA() {
        type = Detail.class;
        coloneSelectBy = null;
    }

    public List<Detail> getDetailOfArticleDette(List<Article> articles, Dette dette) {
        List<Detail> details = new ArrayList<>();
        for (Article article : articles) {
            List<Detail> result = em.createQuery(
                    "SELECT d FROM Detail d WHERE d.article.id = :articleId AND d.dette.id = :detteId",
                    Detail.class)
                    .setParameter("articleId", article.getId())
                    .setParameter("detteId", dette.getId())
                    .getResultList();
            details.addAll(result);
        }
        return details;
    }
}
