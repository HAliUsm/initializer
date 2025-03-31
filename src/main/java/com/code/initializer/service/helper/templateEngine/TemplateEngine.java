package com.code.initializer.service.helper.templateEngine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TemplateEngine {

    public void build(Path template, Path OutputPath) throws IOException {
        // Implementation for building the template
        String TemplatContent = new String(Files.readAllBytes(template));
        // This is where you would process the template files and generate the output
    }
}
