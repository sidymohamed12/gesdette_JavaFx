package com.dette.repository.JPA;

import java.util.List;

import javax.persistence.NoResultException;

import com.dette.core.database.implement.RepositoryJpaImpl;
import com.dette.entities.Article;
import com.dette.entities.Dette;
import com.dette.repository.implement.ArticleRepository;

public class ArticleJPA extends RepositoryJpaImpl<Article> implements ArticleRepository {

    public ArticleJPA() {
        type = Article.class;
        coloneSelectBy = "ref";
    }

    @Override
    public List<Article> articleOfDette(Dette dette) {
        try {
            String sql = "SELECT DISTINCT a FROM Detail dt JOIN dt.article a WHERE dt.dette.id = :detteId";
            return em.createQuery(sql, Article.class)
                    .setParameter("detteId", dette.getId())
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
