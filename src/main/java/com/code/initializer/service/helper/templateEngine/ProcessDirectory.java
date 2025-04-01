package com.code.initializer.service.helper.templateEngine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ProcessDirectory {

    public static void processDirectory(String templatePath, String outputPath) throws IOException {
        if(Files.exists(Paths.get(outputPath))){
            Files.createDirectories(Paths.get(outputPath));
        }

        try(Stream<Path> paths = Files.walk(Paths.get(templatePath))) {
            paths.filter(Files::isRegularFile).forEach(path -> {
                Path relativePath = Paths.get(templatePath).relativize(path);
                Path outputFilePath = Paths.get(outputPath).resolve(relativePath);
            });
        }
    }
}
