package com.code.initializer.model;

import lombok.Data;

import java.util.List;

@Data
public class TemplateConstants {

    private String packageName;

    private String moduleName;

    private String author;

    private String endPoint;

    private String DependencyImports;

    private List<String> modelNames;

    private String ApplicationClassName;

    private List<String> crudClassNameUCamel;

    private List<String> crudClassNameLCamel;

    private String configClassNameUCamel;

    private String configClassNameLCamel;

    private List<String> controllerClassNameUCamel;

    private List<String> controllerClassNameLCamel;

    private String requestClassNameUCamel;

    private String requestClassNameLCamel;

    private String responseClassNameUCamel;

    private String responseClassNameLCamel;
}
