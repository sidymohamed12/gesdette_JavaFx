package com.dette.core.database.implement;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;

import com.dette.core.database.DataSource;
import com.dette.entities.Article;
import com.dette.entities.Client;
import com.dette.entities.Dette;
import com.dette.entities.User;
import com.dette.enums.Etat;
import com.dette.enums.Role;
import com.dette.repository.bd.ArticleRepositoryBD;
import com.dette.repository.bd.ClientRepositoryBD;
import com.dette.repository.bd.DetteRepositoryBD;
import com.dette.repository.bd.UserRepositoryBD;
import com.dette.repository.implement.ArticleRepository;
import com.dette.repository.implement.ClientRepository;
import com.dette.repository.implement.DetteRepository;
import com.dette.repository.implement.UserRepository;

public class DataSourceImpl<T> implements DataSource<T> {
    protected final String url = "jdbc:mysql://localhost:3306/ges_dette";
    protected final String user = "root";
    protected final String mdp = "Mohamed2709";
    protected PreparedStatement ps;
    protected Connection conn = null;
    protected ArticleRepository articleRepository;
    protected ClientRepository clientRepository;
    protected DetteRepository detteRepository;
    protected UserRepository userRepository;

    public DataSourceImpl(UserRepositoryBD userRepository, ClientRepositoryBD clientRepository,
                          ArticleRepositoryBD articleRepositoryBD, DetteRepositoryBD detteRepositoryBD) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.articleRepository = articleRepositoryBD;
        this.detteRepository = detteRepositoryBD;

    }
    @Override
    public void connexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, mdp);

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Échec de la connexion à la BD: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ----------------------- GENERATEUR DE REQUETE --------------------
    @Override
    public String generateSql(String action, String tableName, String[] columns, String coloneCondition, Object value) {
        StringBuilder sql = new StringBuilder();

        if ("INSERT".equalsIgnoreCase(action)) {
            sql.append("INSERT INTO ").append(tableName).append(" (");

            // Ajouter les colonnes
            for (int i = 0; i < columns.length; i++) {
                sql.append(columns[i]);
                if (i < columns.length - 1) {
                    sql.append(", ");
                }
            }

            sql.append(") VALUES (");

            // Ajouter les placeholders pour les valeurs
            for (int i = 0; i < columns.length; i++) {
                sql.append("?");
                if (i < columns.length - 1) {
                    sql.append(", ");
                }
            }
            sql.append(")");

        } else if ("UPDATE".equalsIgnoreCase(action)) {
            sql.append("UPDATE ").append(tableName).append(" SET ");

            // Ajouter les colonnes avec leurs placeholders
            for (int i = 0; i < columns.length; i++) {
                sql.append(columns[i]).append(" = ?");
                if (i < columns.length - 1) {
                    sql.append(", ");
                }
            }

            // Ajouter la condition WHERE avec la valeur directement
            if (coloneCondition != null && value != null) {
                sql.append(" WHERE ").append(coloneCondition).append(" = ");
                if (value instanceof String) {
                    sql.append("'").append(value).append("'");
                } else {
                    sql.append(value);
                }
            }

        } else if ("SELECT".equalsIgnoreCase(action)) {
            sql.append("SELECT ");

            // Ajouter les colonnes (ou * si aucune colonne n'est spécifiée)
            if (columns == null || columns.length == 0) {
                sql.append("*");
            } else {
                for (int i = 0; i < columns.length; i++) {
                    sql.append(columns[i]);
                    if (i < columns.length - 1) {
                        sql.append(", ");
                    }
                }
            }

            sql.append(" FROM ").append(tableName);

            // Ajouter la condition WHERE avec la valeur directement
            if (coloneCondition != null && value != null) {
                sql.append(" WHERE ").append(coloneCondition).append(" = ");
                if (value instanceof String) {
                    sql.append("'").append(value).append("'");
                } else {
                    sql.append(value);
                }
            }
        }

        return sql.toString();
    }

    // ---------------------- fusion classe fille et mère -------------------

    @Override
    public Field[] getAllFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields(); // Champs déclarés dans la classe actuelle
        Class<?> superClass = clazz.getSuperclass(); // Classe parente

        if (superClass != null) {
            Field[] superFields = getAllFields(superClass); // Appel récursif pour récupérer les champs de la classe
                                                            // parente
            Field[] combinedFields = new Field[fields.length + superFields.length];
            System.arraycopy(fields, 0, combinedFields, 0, fields.length);
            System.arraycopy(superFields, 0, combinedFields, fields.length, superFields.length);
            return combinedFields;
        }

        return fields;
    }

    // --------------------- generateur de set pour update-insert ---------------

    @Override
    public void setFields(T value) throws SQLException {
        Field[] fields = getAllFields(value.getClass());
        int index = 1;

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (!field.getName().equals("id") 
                        && !field.getName().equals("client")
                        && !field.getName().equals("details")
                        && !field.getName().equals("payements")
                        && !field.getName().equals("montantRestant")
                        && !field.getName().equals("dettes")) {
                    Object fieldValue = field.get(value);
                    System.out.println(field.getName());
                    if (fieldValue instanceof String) {
                        ps.setString(index, (String) fieldValue);
                    } else if (fieldValue instanceof Integer) {
                        ps.setInt(index, (Integer) fieldValue);
                    } else if (fieldValue instanceof Double) {
                        ps.setDouble(index, (Double) fieldValue);
                    } else if (fieldValue instanceof Boolean) {
                        ps.setBoolean(index, (Boolean) fieldValue);
                    } else if (fieldValue instanceof LocalDateTime) {
                        ps.setTimestamp(index, Timestamp.valueOf((LocalDateTime) fieldValue));
                    } else if (fieldValue instanceof Enum) {
                        ps.setInt(index, ((Enum<?>) fieldValue).ordinal()+1);
                    } else if (field.getName().equals("user")) {
                        User user = (User) field.get(value);
                        if (user != null && user.getId() != 0) {
                            ps.setInt(index, detteRepository.count()+1);
                        } else {
                            ps.setNull(index, Types.INTEGER);
                        }
                    } else if (field.getName().equals("dette")) {
                        Dette dette = (Dette) field.get(value);
                        if (dette != null && dette.getId() != null && dette.getId() != 0) {
                            ps.setInt(index, dette.getId());
                        } else {
                            ps.setNull(index, Types.INTEGER);
                        }
                    }  else if (field.getName().equals("article")) {
                        Article article = (Article) field.get(value);
                        if (article != null && article.getId() != 0) {
                            ps.setInt(index, article.getId());
                        } else {
                            ps.setNull(index, Types.INTEGER);
                        }
                    } else if (field.getName().equals("clientD")) {
                        Client client = (Client) field.get(value);
                        if (client != null && client.getId() != 0) {
                            ps.setInt(index, client.getId());
                        } else {
                            ps.setNull(index, Types.INTEGER);
                        }
                    }

                    index++;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Erreur lors du mapping de l'objet au PreparedStatement", e);
            }
        }
    }

    // ---------------------------- EXECUTE UPDATE --------------------------
    @Override
    public int executeUpdate(String query, T value) {
        int lignes = 0;
        try {

            connexion();
            initPreparedStatement(query);
            setFields(value);
            lignes = ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'exécution de la requête : " +
                    e.getMessage());
        }
        return lignes;
    }

    // ---------------------------- EXECUTE QUERY --------------------------
    @Override
    public ResultSet executeQuery(String query) {
        ResultSet rs = null;
        try {
            connexion();
            initPreparedStatement(query);
            rs = ps.executeQuery();

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'exécution de la requête : " +
                    e.getMessage());
        }
        return rs;
    }

    @Override
    public void initPreparedStatement(String sql) throws SQLException {
        if (sql.toUpperCase().trim().startsWith("INSERT")) {
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } else {
            ps = conn.prepareStatement(sql);
        }
    }

    public T converToObjet(ResultSet rs, Class<T> clazz) throws SQLException {
        T entity;
        try {
            entity = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = getAllFields(entity.getClass());

            for (Field field : fields) {
                field.setAccessible(true);

                try {
                    if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
                        field.set(entity, rs.getInt(field.getName()));
                    } else if (field.getType().equals(String.class)) {
                        field.set(entity, rs.getString(field.getName()));
                    } else if (field.getType().equals(Double.class) || field.getType().equals(double.class)) {
                        field.set(entity, rs.getDouble(field.getName()));
                    } else if (field.getType().equals(Boolean.class)) {
                        field.set(entity, rs.getBoolean(field.getName()));
                    } else if (field.getType().equals(LocalDateTime.class)) {
                        Timestamp timestamp = rs.getTimestamp(field.getName());
                        if (timestamp != null) {
                            field.set(entity, timestamp.toLocalDateTime());
                        } else {
                            field.set(entity, null);
                        }
                    } else if (field.getType().equals(Role.class)) {
                        field.set(entity, Role.getRoleById(rs.getInt("roleId")));
                    } else if (field.getType().equals(Etat.class)) {
                        field.set(entity, Etat.getEtatById(rs.getInt("etatId")));
                    } else if (field.getName().equals("user")) {
                        field.set(entity, userRepository.selectById(rs.getInt("userId")));
                    } else if (field.getName().equals("client") || field.getName().equals("clientD")) {
                        field.set(entity, clientRepository.selectById(rs.getInt("clientId")));
                    } else if (field.getName().equals("article")) {
                        field.set(entity, articleRepository.selectById(rs.getInt("articleId")));
                    } else if (field.getName().equals("dette")) {
                        field.set(entity, detteRepository.selectById(rs.getInt("detteId")));
                    } 
                } catch (SQLException e) {
                    continue;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la conversion de l'objet", e );
        }
        return entity;
    }

}
