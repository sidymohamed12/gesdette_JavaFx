package com.dette.repository.list;

import java.util.List;

import com.dette.core.database.implement.RepositoryListeImplement;
import com.dette.entities.Article;
import com.dette.entities.Dette;
import com.dette.repository.implement.ArticleRepository;

public class ArticleRepositoryList extends RepositoryListeImplement<Article> implements ArticleRepository {

    @Override
    public Article selectBy(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectBy'");
    }

    @Override
    public void update(Article client) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Article selectById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    @Override
    public List<Article> articleOfDette(Dette dette) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'articleOfDette'");
    }

}
