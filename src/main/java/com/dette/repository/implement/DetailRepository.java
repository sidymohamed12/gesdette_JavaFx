package com.dette.repository.implement;

import java.util.List;

import com.dette.core.Repository;
import com.dette.entities.Article;
import com.dette.entities.Detail;
import com.dette.entities.Dette;

public interface DetailRepository extends Repository<Detail> {
    Detail selectBy(String name);

    Detail selectById(int id);

    List<Detail> getDetailOfArticleDette(List<Article> articles, Dette dette);

    void update(Detail value);

}
