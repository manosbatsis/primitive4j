package com.github.manosbatsis.domainprimitives.spring.autoconfigure;

import com.github.manosbatsis.domainprimitives.core.DomainPrimitive;
import com.github.manosbatsis.domainprimitives.spring.FromDomainPrimitiveConverter;
import com.github.manosbatsis.domainprimitives.spring.ToDomainPrimitiveConverter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(DomainPrimitive.class)
@EnableConfigurationProperties(DomainPrimitivesProperties.class)
public class DomainPrimitivesAutoConfiguration {

    @Bean
    FromDomainPrimitiveConverter typedPropertyConverter() {
        return new FromDomainPrimitiveConverter();
    }

    @Bean
    ToDomainPrimitiveConverter toTypedPropertyConverter() {
        return new ToDomainPrimitiveConverter();
    }
}
