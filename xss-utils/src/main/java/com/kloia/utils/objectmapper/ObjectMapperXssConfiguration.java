package com.kloia.utils.objectmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kloia.utils.xss.XssSafeStringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class ObjectMapperXssConfiguration {

    @Value("${xss.enabled:true}")
    private boolean xssEnabled;

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        ObjectMapper newMapper = objectMapper.copy();
        SimpleModule simpleModule = new SimpleModule();
        if (xssEnabled) {
            simpleModule.addDeserializer(String.class, new XssSafeStringDeserializer());
        }
        newMapper.registerModule(simpleModule);
        return new MappingJackson2HttpMessageConverter(newMapper);
    }

}
