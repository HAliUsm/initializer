package com.code.initializer.model;

import io.americanexpress.synapse.service.imperative.model.BaseServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CreateInitializerRequest implements BaseServiceRequest {

    @NotBlank
    private MultipartFile yaml;

    @NotBlank
    private String moduleName;

    @NotBlank
    private String packageName;

    @NotBlank
    private String author;

    @NotBlank
    private String moduleType;

    @NotBlank
    private List<String> dependencyList;

    @NotBlank
    private String description;
}
