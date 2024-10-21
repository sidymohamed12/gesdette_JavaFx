package com.dette.repository.list;

import org.mindrot.jbcrypt.BCrypt;

import com.dette.core.database.implement.RepositoryListeImplement;
import com.dette.entities.User;
import com.dette.repository.implement.UserRepository;

public class UserRepositoryListe extends RepositoryListeImplement<User> implements UserRepository {

    @Override
    public User selectBy(String name) {
        return listes.stream()
                .filter(user -> user.getLogin().compareTo(name) == 0)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(User user) {
        User us = listes.stream()
                .filter(value -> value.getLogin().compareTo(user.getLogin()) == 0)
                .findFirst()
                .orElse(null);
        if (us != null) {
            us.setLogin(user.getLogin());
            us.setPassword(user.getPassword());
            us.setRole(user.getRole());
            us.setEtat(user.getEtat());
        }
    }

    @Override
    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    @Override
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    @Override
    public User selectById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    @Override
    public User authentification(String login, String password) {
        User user = selectBy(login);
        if (user!=null && verifyPassword(password, user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }
}
