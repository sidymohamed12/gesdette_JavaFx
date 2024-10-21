package com.dette.core.factory.Impl;

import com.dette.core.Repository;
import com.dette.core.Service;
import com.dette.core.factory.IFactoryService;
import com.dette.entities.Article;
import com.dette.entities.Client;
import com.dette.entities.Detail;
import com.dette.entities.Dette;
import com.dette.entities.Payement;
import com.dette.entities.User;
import com.dette.repository.implement.ArticleRepository;
import com.dette.repository.implement.ClientRepository;
import com.dette.repository.implement.DetteRepository;
import com.dette.repository.implement.PayementRepository;
import com.dette.repository.implement.DetailRepository;
import com.dette.repository.implement.UserRepository;
import com.dette.services.ArticleService;
import com.dette.services.ClientService;
import com.dette.services.DetteService;
import com.dette.services.PayementService;
import com.dette.services.DetailService;
import com.dette.services.UserService;

public class FactoryService<T> implements IFactoryService<T> {
    private final Service<T> service;

    public FactoryService(Class<T> clazz, Repository<T> repository) {
        if (Client.class.isAssignableFrom(clazz)) {
            this.service = (Service<T>) new ClientService((ClientRepository) repository);
        } else if (User.class.isAssignableFrom(clazz)) {
            this.service = (Service<T>) new UserService((UserRepository) repository);
        } else if (Article.class.isAssignableFrom(clazz)) {
            this.service = (Service<T>) new ArticleService((ArticleRepository) repository);
        } else if (Dette.class.isAssignableFrom(clazz)) {
            this.service = (Service<T>) new DetteService((DetteRepository) repository);
        } else if (Detail.class.isAssignableFrom(clazz)) {
            this.service = (Service<T>) new DetailService((DetailRepository) repository);
        } else if (Payement.class.isAssignableFrom(clazz)) {
            this.service = (Service<T>) new PayementService((PayementRepository) repository);
        } else {
            throw new IllegalArgumentException("Unsupported entity type: " + clazz.getName());
        }
    }

    @Override
    public Service<T> createService() {
        return service;
    }
}
