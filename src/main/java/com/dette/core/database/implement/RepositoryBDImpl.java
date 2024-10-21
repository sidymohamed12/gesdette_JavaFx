package com.dette.core.database.implement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dette.core.database.RepositoryBD;
import com.dette.repository.bd.ArticleRepositoryBD;
import com.dette.repository.bd.ClientRepositoryBD;
import com.dette.repository.bd.DetteRepositoryBD;
import com.dette.repository.bd.UserRepositoryBD;

public abstract class RepositoryBDImpl<T> extends DataSourceImpl<T> implements RepositoryBD<T> {
    protected String tableName;
    protected String colomnSelectBy;
    protected Class<T> clazz;
    protected String[] colones;
    
    public RepositoryBDImpl(UserRepositoryBD userRepository, ClientRepositoryBD clientRepository,
                             ArticleRepositoryBD articleRepositoryBD, DetteRepositoryBD detteRepositoryBD) {

        super(userRepository, clientRepository, articleRepositoryBD, detteRepositoryBD);

    }

    // SQL statement pour debug
    // System.out.println("Executing SQL: " + ps.toString());

    @Override
    public List<T> selectAll() {
        List<T> entities = new ArrayList<>();
        try {
            ResultSet res = executeQuery(generateSql("SELECT", tableName, null, null, null));
            while (res.next()) {
                entities.add(converToObjet(res, clazz));
            }
        } catch (SQLException e) {
            System.out.println("Erreur requête : " + e.getMessage());
        }
        return entities;
    }

    @Override
    public T selectById(int id) {
        T entity = null;
        try {
            ResultSet res = executeQuery(generateSql("SELECT", tableName, null, "id", id));
            if (res.next()) {
                entity = converToObjet(res, clazz);
            }
        } catch (SQLException e) {
            System.out.println("Erreur requête : " + e.getMessage());
        }
        return entity;
    }

    @Override
    public T selectBy(String name) {
        T entity = null;
        try {
            ResultSet res = executeQuery(generateSql("SELECT", tableName, null, colomnSelectBy, name));
            if (res.next()) {
                entity = converToObjet(res, clazz);
            }
        } catch (SQLException e) {
            System.out.println("Erreur requête : " + e.getMessage());
        }
        return entity;
    }

    @Override
    public void insert(T value) {
        String query = generateSql("INSERT", tableName, colones, null, null);

        try {
            executeUpdate(query, value);
            System.out.println("Executing SQL: " + ps.toString());
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                Method setIdMethod = value.getClass().getMethod("setId", Integer.class);
                setIdMethod.invoke(value, rs.getInt(1));
            }
        } catch (SQLException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("Erreur lors de l'exécution de la requête : " + e.getMessage());
        }

    }

    @Override
    public void update(T value) {
        try {
            Method getIdMethod = value.getClass().getMethod("getId");
            String query = generateSql("UPDATE", tableName, colones, "id", getIdMethod.invoke(value));
            executeUpdate(query, value);
            // System.out.println("Executing SQL: " + ps.toString());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int count() {
        int count = 0;
        try {
            String sql = String.format("SELECT MAX(id) FROM %s", tableName);
            connexion();
            initPreparedStatement(sql);
            try (ResultSet res = ps.executeQuery()) {
                if (res.next()) {
                    return res.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur requête : " + e.getMessage());
        }
        return count;
    }

}
