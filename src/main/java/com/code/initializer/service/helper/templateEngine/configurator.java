package com.code.initializer.service.helper.templateEngine;

public class configurator {
}

//import org.openapitools.codegen.*;
//        import org.openapitools.codegen.config.CodegenConfigurator;
//import org.openapitools.codegen.DefaultGenerator;
//
//import java.nio.file.Paths;
//import java.util.HashMap;
//import java.util.Map;

//public class OpenApiGeneratorRunner {
//
//    public static void main(String[] args) {
//        // 1. Configure the generator
//        CodegenConfigurator configurator = new CodegenConfigurator()
//                .setGeneratorName("java") // The target language
//                .setInputSpec("path/to/your/openapi.yaml") // Path to your OpenAPI spec
//                .setOutputDir("generated-code") // Output directory
//                .setTemplateDir("templates") // Path to your custom templates
//                .setApiPackage("io.americanexpress.sample.client.weather.api")
//                .setModelPackage("io.americanexpress.sample.client.weather.model")
//                .setInvokerPackage("io.americanexpress.sample.client.weather")
//                .setGroupId("com.example")
//                .setArtifactId("weather-client")
//                .setArtifactVersion("1.0.0");
//
//        // 2. Add custom template values
//        Map<String, Object> additionalProperties = new HashMap<>();
//        additionalProperties.put("companyName", "American Express Travel Related Services Company, Inc.");
//        additionalProperties.put("clientName", "weather");
//        additionalProperties.put("clientClassName", "WeatherClient");
//        additionalProperties.put("headersFactoryClassName", "WeatherClientHeadersFactory");
//        additionalProperties.put("errorHandlerClassName", "WeatherResponseErrorHandler");
//        additionalProperties.put("configClassName", "WeatherClientConfig");
//        additionalProperties.put("requestModel", "WeatherRequest");
//        additionalProperties.put("responseModel", "WeatherResponse");
//        additionalProperties.put("httpMethod", "GET");
//        additionalProperties.put("package", "io.americanexpress.sample");
//        additionalProperties.put("clientVariableName", "weather");
//        additionalProperties.put("currentYear", java.time.Year.now().getValue());
//
//        configurator.setAdditionalProperties(additionalProperties);
//
//        // 3. Set any specific template files you want to use (optional)
//        configurator.setApiNameSuffix("Client");
//        configurator.setModelNameSuffix("");
//
//        // 4. Configure supporting files
//        configurator.setSupportingFilesToGenerate(
//                "WeatherClient.java",
//                "WeatherClientHeadersFactory.java",
//                "WeatherClientConfig.java",
//                "WeatherResponseErrorHandler.java",
//                "WeatherRequest.java",
//                "WeatherResponse.java"
//        );
//
//        // 5. Create the generator and run it
//        try {
//            ClientOptInput clientOptInput = configurator.toClientOptInput();
//            DefaultGenerator generator = new DefaultGenerator();
//            generator.opts(clientOptInput).generate();
//
//            System.out.println("Code generation completed successfully!");
//        } catch (Exception e) {
//            System.err.println("Code generation failed: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}