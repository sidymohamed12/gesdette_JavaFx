package com.dette.services.servicespe;

import java.util.List;

import com.dette.core.Service;
import com.dette.entities.Article;
import com.dette.entities.Detail;
import com.dette.entities.Dette;

public interface IDetailService extends Service<Detail> {
    List<Detail> getDetailOfArticleDette(List<Article> articles, Dette dette);

    void modifier(Detail value);
}
