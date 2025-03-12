package com.github.manosbatsis.typedidref.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

    @Bean
    FromTypedPropertyConverter typedPropertyConverter() {
        return new FromTypedPropertyConverter();
    }

    @Bean
    ToTypedPropertyConverter toTypedPropertyConverter() {
        return new ToTypedPropertyConverter();
    }
}
