package com.dette.services;

import java.util.List;

import com.dette.entities.Article;
import com.dette.entities.Dette;
import com.dette.repository.implement.ArticleRepository;
import com.dette.services.servicespe.IArticleService;

public class ArticleService implements IArticleService {

    private ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public void create(Article value) {
        int id = count() + 1;
        int size = String.valueOf(id).length();
        value.setRef("ART" + "0".repeat(size > 4 ? 0 : 4 - size) + id);
        value.onPrePersist();
        articleRepository.insert(value);
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.selectAll();
    }

    @Override
    public Article getBy(String name) {

        return articleRepository.selectBy(name);
    }

    @Override
    public int count() {
        return articleRepository.count();
    }

    @Override
    public void modifier(Article article) {
        article.onPreUpdated();
        articleRepository.update(article);
    }

    @Override
    public List<Article> getArticlesDette(Dette dette) {
        return articleRepository.articleOfDette(dette);
    }

}
