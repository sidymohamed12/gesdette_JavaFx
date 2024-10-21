package com.dette.repository.implement;

import java.util.List;

import com.dette.core.Repository;
import com.dette.entities.Article;
import com.dette.entities.Dette;

public interface ArticleRepository extends Repository<Article> {
    Article selectBy(String name);

    Article selectById(int id);

    void update(Article client);

    List<Article> articleOfDette(Dette dette);
}
