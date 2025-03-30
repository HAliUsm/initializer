package com.code.initializer.service;

import com.code.initializer.model.CreateInitalizerResponse;
import com.code.initializer.model.CreateInitializerRequest;
import com.code.initializer.service.yamlSurgeon.YamlParser;
import io.americanexpress.synapse.framework.exception.ApplicationClientException;
import io.americanexpress.synapse.framework.exception.model.ErrorCode;
import io.americanexpress.synapse.service.imperative.service.BaseService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

public class CreateInitializerService extends BaseService<CreateInitializerRequest, CreateInitalizerResponse> {


    @Override
    protected CreateInitalizerResponse doExecute(CreateInitializerRequest request) {

        Optional.ofNullable(request.getYaml())
                .orElseThrow(()-> new ApplicationClientException("Yaml file is required", ErrorCode.GENERIC_4XX_ERROR));

        File convertYaml = new File(request.getYaml().getOriginalFilename());

        try{
            FileOutputStream fileOutputStream = new FileOutputStream(convertYaml);
            fileOutputStream.write(request.getYaml().getBytes());
            fileOutputStream.close();

            YamlParser yamlParser = new YamlParser(convertYaml);
            var endPoint = yamlParser.getEndpointCrud();
            var endPointData = yamlParser.extractEndPointDetails();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}


