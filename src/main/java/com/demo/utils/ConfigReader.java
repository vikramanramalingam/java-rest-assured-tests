package com.demo.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@SuppressWarnings("unchecked")
public class ConfigReader {

    private static Map<String, Map<String, String>> baseUrls;
    private static Map<String, Map<String, Object>> endpoints;
    private static Map<String, Object> config;
    private static final String DEFAULT_ENV = "dev";

    static {
        try {
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            baseUrls = objectMapper.readValue(new File("src/main/java/com/globepoc/endpoints/base_urls.yml"), Map.class);
            endpoints = objectMapper.readValue(new File("src/main/java/com/globepoc/endpoints/api_endpoints.yml"), Map.class);
            config = objectMapper.readValue(new File("src/test/resources/config.yml"), Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load baseUrls.yaml", e);
        }
    }

    public static String getBaseUrl() {
        String env = System.getProperty("env", DEFAULT_ENV);
        return baseUrls.get("base_urls").getOrDefault(env, baseUrls.get("base_urls").get(DEFAULT_ENV));
    }

    public static String getProperty(String key) {
        String[] keys = key.split("\\."); // Support nested properties
        Map<String, Object> current = config;

        for (int i = 0; i < keys.length - 1; i++) {
            if (current.containsKey(keys[i]) && current.get(keys[i]) instanceof Map) {
                current = (Map<String, Object>) current.get(keys[i]);
            } else {
                throw new IllegalArgumentException("Invalid key path: " + key);
            }
        }

        String finalKey = keys[keys.length - 1];
        Object value = current.get(finalKey);
        if (value == null) {
            throw new IllegalArgumentException("Key not found: " + key);
        }

        return value.toString();
    }


    public static Map<String, String> getPath(String... keys) {
        Object current = endpoints.get("endpoints");
        for (String key : keys) {
            if (current instanceof Map) {
                current = ((Map<?, ?>) current).get(key);
            } else {
                throw new IllegalArgumentException("Invalid key path: " + String.join(".", keys));
            }
        }
        if (current instanceof Map) {
            return ((Map<?, ?>) current).entrySet().stream()
                    .collect(Collectors.toMap(
                            e -> (String) e.getKey(),
                            e -> (String) e.getValue()
                    ));
        } else {
            throw new IllegalArgumentException("Expected a Map but found: " + current.getClass().getSimpleName());
        }
    }

    public static String readJsonFile(String filePath) throws Exception {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    
}
