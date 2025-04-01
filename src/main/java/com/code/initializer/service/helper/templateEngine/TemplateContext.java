package com.code.initializer.service.helper.templateEngine;

import com.code.initializer.model.TemplateConstants;

import java.util.HashMap;
import java.util.Map;

public class TemplateContext {


    public Map<String, Object> context(TemplateConstants templateConstants) {
        Map<String, Object> context = new HashMap<>();

        context.put("packageName", templateConstants.getPackageName());
        context.put("moduleName", templateConstants.getModuleName());
        context.put("author", templateConstants.getAuthor());
        context.put("endPoint", templateConstants.getEndPoint());
        context.put("DependencyImports", templateConstants.getDependencyImports());
        context.put("modelNames", templateConstants.getModelNames());
        context.put("applicationClassName", templateConstants.getApplicationClassName());
        context.put("crudClassNameUCamel", templateConstants.getCrudClassNameUCamel());
        context.put("crudClassNameLCamel", templateConstants.getCrudClassNameLCamel());
        context.put("configClassNameUCamel", templateConstants.getConfigClassNameUCamel());
        context.put("configClassNameLCamel", templateConstants.getConfigClassNameLCamel());
        context.put("controllerClassNameUCamel", templateConstants.getControllerClassNameUCamel());
        context.put("controllerClassNameLCamel", templateConstants.getControllerClassNameLCamel());
        context.put("requestClassNameUCamel", templateConstants.getRequestClassNameUCamel());
        context.put("requestClassNameLCamel", templateConstants.getRequestClassNameLCamel());
        context.put("responseClassNameUCamel", templateConstants.getResponseClassNameUCamel());
        context.put("responseClassNameLCamel", templateConstants.getResponseClassNameLCamel());
        return context;
    };
}
