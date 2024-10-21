package com.dette.core.services;

import java.util.Map;
import java.io.InputStream;
import org.yaml.snakeyaml.Yaml;

public class YamlServiceImpl implements YamlService {

    private String path = "config.yaml";

    @Override
    public Map<String, Object> loadYaml() {
        return this.loadYaml(path);
    }

    @Override
    public Map<String, Object> loadYaml(String path) {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
        return yaml.load(inputStream);
    }

}
