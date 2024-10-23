package com.dette.services;

import java.util.List;

import com.dette.entities.Article;
import com.dette.entities.Detail;
import com.dette.entities.Dette;
import com.dette.repository.implement.DetailRepository;
import com.dette.services.servicespe.IDetailService;

public class DetailService implements IDetailService {

    private DetailRepository detailRepository;

    public DetailService(DetailRepository detailRepository) {
        this.detailRepository = detailRepository;
    }

    @Override
    public void create(Detail value) {
        value.onPrePersist();
        detailRepository.insert(value);
    }

    @Override
    public List<Detail> findAll() {
        return detailRepository.selectAll();
    }

    @Override
    public Detail getBy(String name) {
        return detailRepository.selectBy(name);
    }

    @Override
    public int count() {
        return detailRepository.count();
    }

    @Override
    public List<Detail> getDetailOfArticleDette(List<Article> articles, Dette dette) {
        return detailRepository.getDetailOfArticleDette(articles, dette);
    }

    @Override
    public void modifier(Detail value) {
        detailRepository.update(value);
    }

}
