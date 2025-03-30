package com.code.initializer.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.americanexpress.synapse.api.rest.imperative.config.BaseApiImperativeRestConfig;
import io.americanexpress.synapse.api.rest.imperative.interceptor.MetricInterceptor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ComponentScan(InitilaizerConfig.PACKAGE_NAME)
@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:application.properties")
public class InitilaizerConfig extends BaseApiImperativeRestConfig {

    public static final String PACKAGE_NAME = "com.code.initializer";

    public static final String ENPOINT_NAME = "/initializer/v1/modules";

    public InitilaizerConfig(ObjectMapper defaultObjectMapper, MetricInterceptor interceptor) {
        super(defaultObjectMapper, interceptor);
    }
}
