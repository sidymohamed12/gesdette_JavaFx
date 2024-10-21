package com.dette.services;

import com.dette.entities.User;
import com.dette.repository.implement.UserRepository;
import com.dette.services.servicespe.IUserService;

import java.util.List;

public class UserService implements IUserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void create(User user) {
        user.setPassword(userRepository.hashPassword(user.getPassword()));
        user.onPrePersist();
        userRepository.insert(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.selectAll();
    }

    @Override
    public User getBy(String login) {
        return userRepository.selectBy(login);
    }

    @Override
    public int count() {
        return userRepository.count();
    }

    @Override
    public void modifier(User user) {
        user.onPreUpdated();
        userRepository.update(user);
    }

    @Override
    public User connexion(String login, String password) {
        return userRepository.authentification(login, password);
    }
}
