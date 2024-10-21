package com.dette.repository.bd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dette.core.database.implement.RepositoryBDImpl;
import com.dette.entities.Article;
import com.dette.entities.Dette;
import com.dette.repository.implement.ArticleRepository;

public class ArticleRepositoryBD extends RepositoryBDImpl<Article> implements ArticleRepository {

    public ArticleRepositoryBD() {
        super(null,null,null,null);
        clazz = Article.class;
        tableName = "article";
        colomnSelectBy = "ref";
        colones = new String[] { "libelle", "ref", "qteStock", "prix", "createdAt", "updatedAt" };
    }

    @Override
    public List<Article> articleOfDette(Dette dette) {
         List<Article> articles = new ArrayList<>();
        try {
            String sql = String.format("SELECT DISTINCT a.*, dt.qteVendu FROM detaildette dt JOIN article a ON a.id = dt.articleId WHERE dt.detteId = %d", dette.getId());
            ResultSet res = executeQuery(sql);
            while (res.next()) {
                articles.add(converToObjet(res, Article.class));
            }
        } catch (SQLException e) {
            System.out.println("Erreur requête : " + e.getMessage());
        }
        return articles;
    }


}
