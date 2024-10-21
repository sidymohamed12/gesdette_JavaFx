package com.dette.repository.JPA;

import org.mindrot.jbcrypt.BCrypt;

import com.dette.core.database.implement.RepositoryJpaImpl;
import com.dette.entities.User;
import com.dette.repository.implement.UserRepository;

public class UserJPA extends RepositoryJpaImpl<User> implements UserRepository {

    public UserJPA() {
        type = User.class;
        coloneSelectBy = "login";
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
    public User authentification(String login, String password) {
        try {
            User user = selectBy(login);

            if (user != null && verifyPassword(password, user.getPassword())) {
                return user;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
