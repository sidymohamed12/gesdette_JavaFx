package com.dette.core.factory.Impl;

import java.util.Map;

import com.dette.core.Repository;
import com.dette.core.factory.IFactoryRepo;
import com.dette.core.services.YamlService;
import com.dette.core.services.YamlServiceImpl;
import com.dette.entities.Article;
import com.dette.entities.Client;
import com.dette.entities.Detail;
import com.dette.entities.Dette;
import com.dette.entities.Payement;
import com.dette.entities.User;

public class FactoryRepo<T> implements IFactoryRepo<T> {
    private final Repository<T> repositorie;
    YamlService yamlService;

    public FactoryRepo(Class<T> clazz) {

        yamlService = new YamlServiceImpl();
        Map<String, Object> mapYaml = yamlService.loadYaml();

        if (Client.class.isAssignableFrom(clazz)) {

            String yh = (String) ((Map<String, Object>) mapYaml.get("repository")).get("clientRepository");
            String yh2 = (String) ((Map<String, Object>) mapYaml.get("repository")).get("userRepository");
            this.repositorie = (Repository<T>) createRepositoryInstance(yh, yh2,null);

        } else if (User.class.isAssignableFrom(clazz)) {

            String yh = (String) ((Map<String, Object>) mapYaml.get("repository")).get("userRepository");
            this.repositorie = (Repository<T>) createRepositoryInstance(yh, null, null);

        } else if (Article.class.isAssignableFrom(clazz)) {

            String yh = (String) ((Map<String, Object>) mapYaml.get("repository")).get("articleRepository");
            this.repositorie = (Repository<T>) createRepositoryInstance(yh, null, null);

        } else if (Dette.class.isAssignableFrom(clazz)) {

            String yh = (String) ((Map<String, Object>) mapYaml.get("repository")).get("detteRepository");
            String yh2 = (String) ((Map<String, Object>) mapYaml.get("repository")).get("clientRepository");
            this.repositorie = (Repository<T>) createRepositoryInstance(yh, yh2,null);

        } else if (Detail.class.isAssignableFrom(clazz)) {
            String yh = (String) ((Map<String, Object>) mapYaml.get("repository")).get("detailRepository");
            String yh2 = (String) ((Map<String, Object>) mapYaml.get("repository")).get("articleRepository");
            String yh3 = (String) ((Map<String, Object>) mapYaml.get("repository")).get("detteRepository");
            this.repositorie = (Repository<T>) createRepositoryInstance(yh, yh2, yh3);

        } else if (Payement.class.isAssignableFrom(clazz)) {
            String yh = (String) ((Map<String, Object>) mapYaml.get("repository")).get("payementRepository");
            String yh2 = (String) ((Map<String, Object>) mapYaml.get("repository")).get("detteRepository");
            this.repositorie = (Repository<T>) createRepositoryInstance(yh, yh2, null);

        } else {

            throw new IllegalArgumentException("Unsupported entity type: " + clazz.getName());
        }
    }

    private Object createRepositoryInstance(String className, String className2, String className3) {
        try {
            Class<?> clazz = Class.forName(className);

            // Vérification si le constructeur sans paramètre existe
            for (var constructor : clazz.getDeclaredConstructors()) {
                if (constructor.getParameterCount() == 0) {
                    return constructor.newInstance();
                }
            }

            // Vérification si la classe nécessite une dépendance (className2)
            if (className2 != null && className3 == null) {
                Class<?> clazz2 = Class.forName(className2);
                Object instance2 = clazz2.getDeclaredConstructor().newInstance();

                return clazz.getDeclaredConstructor(clazz2).newInstance(instance2);
            }

            // Si deux dépendances (className2 et className3) sont requises
            if (className2 != null && className3 != null) {
                Class<?> clazz2 = Class.forName(className2);
                Class<?> clazz3 = Class.forName(className3);
                Object instance2 = clazz2.getDeclaredConstructor().newInstance();
                Object instance3 = clazz3.getDeclaredConstructor().newInstance();

                return clazz.getDeclaredConstructor(clazz2, clazz3).newInstance(instance2, instance3);
            }

            // Si aucun constructeur approprié n'a été trouvé
            throw new RuntimeException("No suitable constructor found for: " + className);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create repository instance for: " + className, e);
        }
    }

    @Override
    public Repository<T> createRepository() {
        return repositorie;
    }

}
