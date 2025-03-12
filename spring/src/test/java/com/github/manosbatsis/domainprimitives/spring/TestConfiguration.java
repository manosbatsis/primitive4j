package com.github.manosbatsis.domainprimitives.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

    @Bean
    FromDomainPrimitiveConverter typedPropertyConverter() {
        return new FromDomainPrimitiveConverter();
    }

    @Bean
    ToDomainPrimitiveConverter toTypedPropertyConverter() {
        return new ToDomainPrimitiveConverter();
    }
}
