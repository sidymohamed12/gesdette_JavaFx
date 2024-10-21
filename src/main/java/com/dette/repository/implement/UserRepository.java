package com.dette.repository.implement;

import com.dette.core.Repository;
import com.dette.entities.User;

public interface UserRepository extends Repository<User> {
    User selectBy(String name);

    User selectById(int id);

    void update(User user);

    String hashPassword(String plainPassword);

    boolean verifyPassword(String plainPassword, String hashedPassword);

    User authentification(String login, String password);
}
