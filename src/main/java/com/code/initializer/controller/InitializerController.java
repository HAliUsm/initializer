package com.code.initializer.controller;

import com.code.initializer.config.InitilaizerConfig;
import com.code.initializer.model.CreateInitalizerResponse;
import com.code.initializer.model.CreateInitializerRequest;
import com.code.initializer.service.CreateInitializerService;
import io.americanexpress.synapse.api.rest.imperative.controller.BaseCreateImperativeRestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = InitilaizerConfig.ENPOINT_NAME)
public class InitializerController extends BaseCreateImperativeRestController<CreateInitializerRequest, CreateInitalizerResponse, CreateInitializerService> {

}
