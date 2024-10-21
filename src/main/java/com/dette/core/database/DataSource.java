package com.dette.core.database;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DataSource<T> {
    void connexion();

    String generateSql(String action, String tableName, String[] columns, String coloneCondition, Object value);

    void setFields(T value) throws SQLException;

    int executeUpdate(String query, T value);

    // int executeUpdate() throws SQLException;

    ResultSet executeQuery(String query) throws SQLException;

    // ResultSet executeQuery() throws SQLException;

    void initPreparedStatement(String sql) throws SQLException;

    Field[] getAllFields(Class<?> clazz);
}
