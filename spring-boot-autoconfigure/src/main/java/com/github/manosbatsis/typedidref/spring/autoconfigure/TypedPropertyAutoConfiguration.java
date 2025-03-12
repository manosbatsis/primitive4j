package com.github.manosbatsis.typedidref.spring.autoconfigure;

import com.github.manosbatsis.typedidref.core.TypedProperty;
import com.github.manosbatsis.typedidref.spring.FromTypedPropertyConverter;
import com.github.manosbatsis.typedidref.spring.ToTypedPropertyConverter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(TypedProperty.class)
@EnableConfigurationProperties(TypedPropertyProperties.class)
public class TypedPropertyAutoConfiguration {

    @Bean
    FromTypedPropertyConverter typedPropertyConverter() {
        return new FromTypedPropertyConverter();
    }

    @Bean
    ToTypedPropertyConverter toTypedPropertyConverter() {
        return new ToTypedPropertyConverter();
    }
}
