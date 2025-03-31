package com.code.initializer.service;

import com.code.initializer.model.CreateInitializerRequest;
import com.code.initializer.model.TemplateConstants;

public class TemplateConstantMapper {

    public void map(CreateInitializerRequest request, TemplateConstants context ) {
        context.setPackageName(request.getPackageName());
        context.setAuthor(request.getAuthor());
        context.setEndPoint();
    }
}
