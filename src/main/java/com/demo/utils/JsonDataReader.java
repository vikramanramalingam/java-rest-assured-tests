package com.demo.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonDataReader {

    public static List<String> getListFromJson(String filePath, String keyName){
        List<String> values = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(new File(filePath));
            JsonNode targetNode = rootNode.get(keyName);
            if (targetNode != null && targetNode.isArray()) {
                for (JsonNode node : targetNode) {
                    values.add(node.asText());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read JSON file: " + filePath);
        }
        return values;
    }
    
}
