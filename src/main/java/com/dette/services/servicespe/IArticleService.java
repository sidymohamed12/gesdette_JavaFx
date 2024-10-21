package com.dette.services.servicespe;

import java.util.List;

import com.dette.core.Service;
import com.dette.entities.Article;
import com.dette.entities.Dette;

public interface IArticleService extends Service<Article> {
    void modifier(Article article);
    List<Article> getArticlesDette(Dette dette);
}
