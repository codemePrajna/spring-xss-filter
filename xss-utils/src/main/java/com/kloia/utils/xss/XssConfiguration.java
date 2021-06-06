package com.kloia.utils.xss;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "xss.enabled", havingValue = "true")
public class XssConfiguration {

    @Bean
    public XssPreventionFilter xssPreventionFilter() {
        return new XssPreventionFilter();
    }

    @Bean
    XssRemover xssRemover() {
        return new XssRemover();
    }
}
