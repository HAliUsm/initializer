package com.code.initializer.service.helper.yamlSurgeon;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * YamlParser is a utility class that parses a YAML file and extracts endpoint details.
 * It provides methods to get the list of endpoints and their CRUD operations.
 */
@Component
@NoArgsConstructor
public class YamlParser {

    private JsonNode rootNode;

    private final Map<String, JsonNode> refCache = new HashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public YamlParser(File yaml) throws IOException {
            this.rootNode = new ObjectMapper(new  YAMLFactory()).readTree(yaml);
    }

    /**
     * Get the list of endpoints and their CRUD operations
     * @return
     */
    public List<List<String>> getEndpointCrud() {
        Map<String, Map<String, Map<String, String>>> endPointDetails = extractEndPointDetails();
        List<List<String>> pathAndCrud = new ArrayList<>();
        for(Map.Entry<String, Map<String, Map<String, String>>> entry : endPointDetails.entrySet()) {
            String path = entry.getKey();
            List<String> crudList = new ArrayList<>();
            crudList.add(path);
            for (String operation : entry.getValue().keySet()) {
                crudList.add(operation);
            }
            pathAndCrud.add(crudList);
        }
        return pathAndCrud;
    }

    /**
     * Extract the end point details from the yaml file
     * @return Map<String, Map<String, Map<String, String>>>
     */
    public Map<String, Map<String, Map<String, String>>> extractEndPointDetails() {
        Map<String, Map<String, Map<String, String>>> endPoints = new HashMap<>();
        JsonNode pathsNode = rootNode.get("paths");
        if (pathsNode == null) return endPoints;
        pathsNode.fields().forEachRemaining(entry -> {
            Map<String, Map<String, String>> operations = new HashMap<>();
            entry.getValue().fields().forEachRemaining(operationEntry -> {
                Map<String, String> operationDetails = new HashMap<>();
                operationDetails.put("request", extractRequest(operationEntry.getValue().path("requestBody")));
                operationDetails.put("response", extractResponse(operationEntry.getValue().path("responses")));
                operations.put(operationEntry.getKey(), operationDetails);
            });
            endPoints.put(entry.getKey(), operations);
        });
        return endPoints;
    }

    /**
     * Extract the request from the request node
     * @param requestNode
     * @return String Request
     */
    private String extractRequest(JsonNode requestNode) {
        JsonNode schemaNode = requestNode.path("content").path("application/json").path("schema");
        if(schemaNode.has("$ref")) {
            JsonNode refNode = getRef(schemaNode.path("$ref").asText());
            return refNode!= null ? refNode.toString() : null;
        }
        else {
            return schemaNode.toString();
        }
    }

    /**
     * Extract the response from the response node
     * @param responseNode
     * @return String Reesponse
     */
    private String extractResponse(JsonNode responseNode) {
        StringBuilder responses = new StringBuilder();
        responseNode.fields().forEachRemaining(entry -> {
            JsonNode schemaNode = entry.getValue()
                    .path("content").path("application/json")
                    .path("schema");
            responses.append(entry.getKey()).append(": ")
                    .append(schemaNode.has("$ref") ? getRef(schemaNode.get("$ref")
                                                                    .asText()) : schemaNode)
                    .append("\n");
        });
        return responses.toString().trim();
    }

    /**
     * Get the reference node from the root node
     * @param ref
     * @return the ref node
     */
    private JsonNode getRef(String ref) {
        if(refCache.containsKey(ref)) {
            return refCache.get(ref);
        }
        String[] parts = ref.split("/");
        JsonNode currentNode = rootNode;

        for(String part : parts) {
            if(part.isEmpty()) continue;
            currentNode = currentNode.get(part);
            if(currentNode == null) return null;
        }
        while(currentNode != null && currentNode.has("$ref")) {
            String nextRef = currentNode.get("$ref").asText().replace("#/", "");
            currentNode = getRef(nextRef);
        }
        refCache.put(ref, currentNode);
        return currentNode;
    }
}
