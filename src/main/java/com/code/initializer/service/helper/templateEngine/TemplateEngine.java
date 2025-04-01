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


    public void build(Path templatePath, Path OutputPath, TemplateConstants templateConstants) throws IOException {
        // Implementation for building the template
        String TemplatContent = new String(Files.readAllBytes(Paths.get(TEMPLATE_PATH)));
        // This is where you would process the template files and generate the output

        Template template = mustache.compile(TemplatContent);
        var result = templateContext.context(templateConstants);
        Files.write(OutputPath, result.toString().getBytes());
    }
}
