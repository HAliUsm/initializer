package com.code.initializer.service;

import com.code.initializer.model.CreateInitalizerResponse;
import com.code.initializer.model.CreateInitializerRequest;
import io.americanexpress.synapse.service.imperative.service.BaseService;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.parser.OpenAPIV3Parser;
import lombok.SneakyThrows;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class CreateInitializerService extends BaseService<CreateInitializerRequest, CreateInitalizerResponse> {

    private static final String OUTPUT_DIRECTORY = "generated-code/";
    OpenAPI openAPI;
    String autherName;

    @SneakyThrows
    @Override
    protected CreateInitalizerResponse doExecute(CreateInitializerRequest request) {
        logger.info("Received request: {}", request);
        MultipartFile yamlFile = request.getYaml();
        autherName=request.getAuthor();
        if (yamlFile == null || yamlFile.isEmpty()) {
            logger.error("YAML file is missing or empty.");
            throw new BadRequestException("YAML file is missing or empty.");
        }

        File tempYamlFile = null;
        try {
            tempYamlFile = File.createTempFile("temp", ".yaml");
            yamlFile.transferTo(tempYamlFile);
            logger.info("tempYamlFile path: {}", tempYamlFile.getAbsolutePath());

            openAPI = new OpenAPIV3Parser().read(tempYamlFile.getAbsolutePath());

            if (openAPI == null) {
                logger.error("Error parsing OpenAPI file.");
                throw new BadRequestException("Error parsing OpenAPI file.");
            }
            logger.info("OpenAPI Parsed: {}", openAPI.getInfo().getTitle());

            Map<String, String> generatedFiles = generateCodeFromOpenAPI(openAPI, request.getPackageName(),request);
            logger.info("Generated files: {}", generatedFiles);

            Path zipFilePath = createZip(generatedFiles);
            byte[] zipBytes = Files.readAllBytes(zipFilePath);
            saveZipToFile(zipBytes, request.getModuleName() + ".zip");
            Files.deleteIfExists(zipFilePath);

            return new CreateInitalizerResponse(zipBytes, "success");
        } catch (Exception e) {
            logger.error("Error generating files: {}", e.getMessage(), e);
            e.printStackTrace();
            throw new RuntimeException("Error generating files: " + e.getMessage(), e);
        } finally {
            if (tempYamlFile != null && tempYamlFile.exists()) {
                tempYamlFile.delete();
            }
        }
    }

    private Path createZip(Map<String, String> files) throws IOException {
        Path zipFilePath = Files.createTempFile("initializer", ".zip");
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath.toFile()))) {
            for (Map.Entry<String, String> entry : files.entrySet()) {
                zipOut.putNextEntry(new ZipEntry(entry.getKey()));
                zipOut.write(entry.getValue().getBytes());
                zipOut.closeEntry();
            }
        }
        return zipFilePath;
    }

    private void saveZipToFile(byte[] zipBytes, String fileName) throws IOException {
        File outputDir = new File(OUTPUT_DIRECTORY);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        File outputFile = new File(outputDir, fileName);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(zipBytes);
        }
    }

    private Map<String, String> generateCodeFromOpenAPI(OpenAPI openAPI, String packageName,CreateInitializerRequest request) {
        Map<String, String> generatedFiles = new HashMap<>();

        if (openAPI.getPaths() != null) {
            openAPI.getPaths().forEach((path, pathItem) -> {
                System.out.println("1 requestSchemarequestSchema " + pathItem);
                generateControllerAndModels(path, pathItem, packageName, generatedFiles);
            });
        }

        String sourceFilePath = "template/Config.java"; //  source file
        String textToReplace = "__modulePackage__";
        Map<String ,String> configJava=new HashMap<>();
        configJava.put("__modulePackage__",packageName);
        configJava.put("__authors__",request.getAuthor());
        configJava.put("__authors__",request.getAuthor());
        configJava.put("__configClassNameUCamel__",toCamelCase(request.getModuleName()));
        try {
            generatedFiles.put("Config.java",copyAndReplaceText(sourceFilePath, configJava));
            System.out.println("File copy and replace operation completed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        }

        generatedFiles.put("build.gradle", generateBuildGradle(request));
        return generatedFiles;
    }

    private void generateControllerAndModels(String path, PathItem pathItem, String packageName, Map<String, String> generatedFiles) {
        if (pathItem.getGet() != null) {
            generateControllerAndModelsForOperation(path, pathItem.getGet(), "GET", packageName, generatedFiles);
        }
        if (pathItem.getPost() != null) {
            generateControllerAndModelsForOperation(path, pathItem.getPost(), "POST", packageName, generatedFiles);
        }
        if (pathItem.getPut() != null) {
            generateControllerAndModelsForOperation(path, pathItem.getPut(), "PUT", packageName, generatedFiles);
        }
        if (pathItem.getDelete() != null) {
            generateControllerAndModelsForOperation(path, pathItem.getDelete(), "DELETE", packageName, generatedFiles);
        }
    }

    private void generateControllerAndModelsForOperation(String path, Operation operation, String httpMethod, String packageName, Map<String, String> generatedFiles) {
        logger.info("Generating for path: {}, method: {}", path, httpMethod);
        String className = path.replaceAll("[{}/]", "") + "Controller";
        String requestName = null;
        String responseName = null;

        if (operation.getRequestBody() != null && operation.getRequestBody().getContent() != null && operation.getRequestBody().getContent().get("multipart/form-data") != null) {
            Schema requestSchema = operation.getRequestBody().getContent().get("multipart/form-data").getSchema();
            System.out.println("2 requestSchemarequestSchema " + requestSchema);
            if (requestSchema != null && requestSchema.get$ref() != null) {
                requestName = requestSchema.get$ref().substring(requestSchema.get$ref().lastIndexOf("/") + 1);
                generatedFiles.put(requestName + ".java", generateModel(requestName, requestSchema, packageName));
            }
        } else if (operation.getRequestBody() != null && operation.getRequestBody().getContent() != null && operation.getRequestBody().getContent().get("application/json") != null) {
            Schema requestSchema = operation.getRequestBody().getContent().get("application/json").getSchema();
            if (requestSchema != null && requestSchema.get$ref() != null) {
                requestName = requestSchema.get$ref().substring(requestSchema.get$ref().lastIndexOf("/") + 1);
                generatedFiles.put(requestName + ".java", generateModel(requestName, requestSchema, packageName));
            }
        }

        if (operation.getResponses() != null && operation.getResponses().get("200") != null && operation.getResponses().get("200").getContent() != null && operation.getResponses().get("200").getContent().get("application/zip") != null) {
            responseName = "byte[]";
        } else if (operation.getResponses() != null && operation.getResponses().get("400") != null && operation.getResponses().get("400").getContent() != null && operation.getResponses().get("400").getContent().get("application/json") != null) {
            ApiResponse response = operation.getResponses().get("400");
            Schema responseSchema = response.getContent().get("application/json").getSchema();
            if (responseSchema != null && responseSchema.get$ref() != null) {
                responseName = responseSchema.get$ref().substring(responseSchema.get$ref().lastIndexOf("/") + 1);
                generatedFiles.put(responseName + ".java", generateModel(responseName, responseSchema, packageName));
            }
        }

        generatedFiles.put(className + ".java", generateController(path, requestName, responseName, httpMethod, packageName,autherName));
    }

    private String generateModel(String modelName, Schema schema, String packageName) {
        logger.info("Generating model: {}, schema properties: {}", modelName, schema.getProperties());

        logger.info("openAPI.getComponents().getSchemas().get(modelName).getProperties() :::\n{}",
                openAPI.getComponents().getSchemas().get(modelName).getProperties());


        StringBuilder modelContent = new StringBuilder();
        modelContent.append("package ").append(packageName).append(".model;\n\n");

        modelContent.append("import java.util.*;\n\n");

        modelContent.append("public class ").append(modelName).append(" {\n\n");

        StringBuilder gettersSetters = new StringBuilder();

        System.err.println(" schema.getProperties() " + schema);

        openAPI.getComponents().getSchemas().get(modelName).getProperties().forEach((propertyName, propertyObject) -> {
            Schema propertySchema = (Schema) propertyObject;
            String javaType = mapSchemaTypeToJavaType(propertySchema.getType());
            System.out.println("javaTypejavaTypejavaType " + javaType);
            modelContent.append("    private ").append(javaType).append(" ").append(propertyName).append(";\n");
        });


        modelContent.append(gettersSetters);
        modelContent.append("\n}");
        return modelContent.toString();
    }

    // Helper method to map OpenAPI types to Java types
    private String mapSchemaTypeToJavaType(String openApiType) {
        if (openApiType == null) return "String"; // Default type

        switch (openApiType) {
            case "integer":
                return "Integer";
            case "long":
                return "Long";
            case "float":
                return "Float";
            case "double":
                return "Double";
            case "boolean":
                return "Boolean";
            case "array":
                return "List<Object>"; // Assuming generic list, update based on context
            case "object":
                return "Map<String, Object>"; // Default to Map for nested objects
            default:
                return "String"; // Default case
        }
    }

    // Helper method to capitalize the first letter of a string
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private String generateController(String path, String requestName, String responseName, String httpMethod, String packageName) {
        StringBuilder controllerContent = new StringBuilder();
        String className = path.replaceAll("[{}/]", "") + "Controller";

        controllerContent.append("package ").append(packageName).append(".controller;\n\n")
                .append("import org.springframework.web.bind.annotation.*;\n\n");

        if (requestName != null && responseName != null && !responseName.equals("byte[]")) {
            controllerContent.append("import ").append(packageName).append(".model.").append(requestName).append(";\n");
            controllerContent.append("import ").append(packageName).append(".model.").append(responseName).append(";\n\n");
        } else if (requestName != null && responseName != null && responseName.equals("byte[]")) {
            controllerContent.append("import ").append(packageName).append(".model.").append(requestName).append(";\n\n");
        }

        controllerContent.append("@RestController\n")
                .append("@RequestMapping(\"").append(path).append("\")\n")
                .append("public class ").append(className).append(" {\n\n");

        if (requestName != null) {
            controllerContent.append("    @").append(httpMethod.toUpperCase()).append("Mapping(\"/create\")\n");
            if (responseName != null && responseName.equals("byte[]")) {
                controllerContent.append("    public byte[] create(@RequestBody ").append(requestName).append(" request) {\n");
                controllerContent.append("        // Implement create logic\n");
                controllerContent.append("        return new byte[0];\n");
                controllerContent.append("    }\n\n");
            } else if (responseName != null) {
                controllerContent.append("    public ").append(responseName).append(" create(@RequestBody ").append(requestName).append(" request) {\n");
                controllerContent.append("        // Implement create logic\n");
                controllerContent.append("        return new ").append(responseName).append("();\n");
                controllerContent.append("    }\n\n");
            } else {
                controllerContent.append("    public void create(@RequestBody ").append(requestName).append(" request) {\n");
                controllerContent.append("        // Implement create logic\n");
                controllerContent.append("    }\n\n");
            }
        } else {
            controllerContent.append("    // Request or Response name is null. Implement logic without request/response models.\n\n");
        }

        controllerContent.append("    // Add other CRUD operations based on crudOperations\n")
                .append("}\n");

        return controllerContent.toString();
    }
    private String generateController(String path, String requestName, String responseName, String serviceName, String packageName, String authors) {
        String className = path.replaceAll("[{}/]", "") + "Controller";

        StringBuilder controllerContent = new StringBuilder();
        controllerContent.append("package ").append(packageName).append(".controller;\n\n")
                .append("import ").append(packageName).append(".model.").append(requestName).append(";\n")
                .append("import ").append(packageName).append(".model.").append("__responseClassNameUCamel__").append(";\n")
                .append("import ").append(packageName).append(".service.").append(serviceName).append(";\n")
                .append("import org.springframework.web.bind.annotation.RequestMapping;\n")
                .append("import org.springframework.web.bind.annotation.RestController;\n\n");

        controllerContent.append("/**\n")
                .append(" * {@code ").append(className).append("} •\n")
                .append(" *\n")
                .append(" * @author ").append(authors).append("\n")
                .append(" */\n");

        controllerContent.append("@RequestMapping(\"").append(path).append("\")\n")
                .append("@RestController\n")
                .append("public class ").append(className).append(" extends BaseCreateImperativeRestController<\n")
                .append("        ").append(requestName).append(",\n")
                .append("        ").append("__responseClassNameUCamel__").append(",\n")
                .append("        ").append("__serviceClassNameUCamel__").append("> {\n")
                .append("}\n");

        return controllerContent.toString();
    }

    public String copyAndReplaceText(String resourceFileName, Map<String, String> replacements) throws IOException {
        // Load the file from resources
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceFileName);

        if (inputStream == null) {
            throw new IOException("File not found: " + resourceFileName);
        }

        // Read the file content as a String
        String fileContent;
        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
            fileContent = scanner.useDelimiter("\\A").next(); // Read entire file
        }

        // Replace all placeholders with corresponding values
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            fileContent = fileContent.replace(entry.getKey(), entry.getValue());
        }

        return fileContent;
    }
    private String generateBuildGradle(CreateInitializerRequest request) {
        StringBuilder buildGradleContent = new StringBuilder();

        buildGradleContent.append("plugins {\n")
                .append("    id 'java'\n")
                .append("    id 'org.springframework.boot' version '2.7.4'\n")
                .append("    id 'io.spring.dependency-management' version '1.0.11.RELEASE'\n")
                .append("}\n\n");

        buildGradleContent.append("group = '").append(request.getPackageName()).append("'\n")
                .append("version = '1.0-SNAPSHOT'\n\n");

        buildGradleContent.append("repositories {\n")
                .append("    mavenCentral()\n")
                .append("}\n\n");

        buildGradleContent.append("dependencies {\n")
                .append("    implementation 'org.springframework.boot:spring-boot-starter'\n")
                .append("    implementation 'org.springframework.boot:spring-boot-starter-web'\n");

        // Add dependencies from request
        if (request.getDependencyList() != null && !request.getDependencyList().isEmpty()) {
            for (String dependency : request.getDependencyList()) {
                buildGradleContent.append("    implementation '").append(dependency).append("'\n");
            }
        }

        buildGradleContent.append("    testImplementation 'org.springframework.boot:spring-boot-starter-test'\n")
                .append("}\n\n");

        buildGradleContent.append("test {\n")
                .append("    useJUnitPlatform()\n")
                .append("}\n");

        return buildGradleContent.toString();
    }
    public static String toCamelCase(String input) {
        String sanitized = input.replaceAll("[^a-zA-Z0-9 ]", " ").trim();

        // Convert to camel case
        String[] words = sanitized.split("\\s+");
        StringBuilder camelCaseString = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                if (camelCaseString.length() == 0) {
                    camelCaseString.append(word.toLowerCase());
                } else {
                    camelCaseString.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase());
                }
            }
        }
        return camelCaseString.toString();
    }


}

