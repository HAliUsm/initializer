package com.code.initializer.service.helper.templateEngine;

import com.code.initializer.model.TemplateConstants;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TemplateEngine {

    private static Mustache.Compiler mustache;

    private static TemplateContext templateContext;

    private static final String TEMPLATE_PATH = "resources/templates/service";


    public void build(Path .templatePath, Path OutputPath, TemplateConstants templateConstants) throws IOException {
        // Implementation for building the template
        String TemplatContent = new String(Files.readAllBytes(Paths.get(TEMPLATE_PATH)));
        // This is where you would process the template files and generate the output

        Template template = mustache.compile(TemplatContent);
        var result = templateContext.context(templateConstants);
        Files.write(OutputPath, result.toString().getBytes());
    }
}


//import org.openapitools.codegen.api.TemplatingEngineAdapter;
//import org.openapitools.codegen.api.TemplatingGenerator;
//import java.io.IOException;
//import java.util.Map;
//
//public class ClientTemplateEngine implements TemplatingEngineAdapter {
//
//    @Override
//    public String compileTemplate(TemplatingGenerator generator,
//                                  Map<String, Object> bundle,
//                                  String templateFile) throws IOException {
//        // Read template file
//        String template = readTemplate(generator, templateFile);
//
//        // Apply simple variable substitution
//        for (Map.Entry<String, Object> entry : bundle.entrySet()) {
//            String placeholder = "{{" + entry.getKey() + "}}";
//            String value = entry.getValue() != null ? entry.getValue().toString() : "";
//            template = template.replace(placeholder, value);
//        }
//
//        return template;
//    }
//
//    @Override
//    public String[] getFileExtensions() {
//        return new String[]{"mustache"};
//    }
//
//    private String readTemplate(TemplatingGenerator generator, String templateFile) throws IOException {
//        try (InputStream in = generator.getFullTemplateContents(templateFile);
//             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
//            StringBuilder content = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                content.append(line).append("\n");
//            }
//            return content.toString();
//        }
//    }
//}