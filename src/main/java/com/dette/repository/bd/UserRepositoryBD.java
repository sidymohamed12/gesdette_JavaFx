package com.dette.repository.bd;

import org.mindrot.jbcrypt.BCrypt;

import com.dette.core.database.implement.RepositoryBDImpl;
import com.dette.entities.User;
import com.dette.repository.implement.UserRepository;

public class UserRepositoryBD extends RepositoryBDImpl<User> implements UserRepository {

    public UserRepositoryBD() {
        super(null, null, null, null);
        clazz = User.class;
        tableName = "users";
        colomnSelectBy = "login";
        colones = new String[] { "login", "password", "roleId", "etat", "createdAt", "updatedAt" };
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

        User user = selectBy(login);

        if (user != null && verifyPassword(password, user.getPassword())) {
            return user;
        } else {
            return null;
        }

    }

}
