/**
 * Copyright (C) 2024-2025 Manos Batsis
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with this program. If not, see
 * <a href="https://www.gnu.org/licenses/lgpl-3.0.html">https://www.gnu.org/licenses/lgpl-3.0.html</a>.
 */
package com.github.manosbatsis.domainprimitives.spring.autoconfigure;

import com.github.manosbatsis.domainprimitives.core.DomainPrimitive;
import com.github.manosbatsis.domainprimitives.spring.FromDomainPrimitiveGenericConverter;
import com.github.manosbatsis.domainprimitives.spring.StringToDomainPrimitiveConverterFactory;
import com.github.manosbatsis.domainprimitives.spring.ToDomainPrimitiveGenericConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnClass(DomainPrimitive.class)
@EnableConfigurationProperties(DomainPrimitivesProperties.class)
public class DomainPrimitivesAutoConfiguration implements WebMvcConfigurer {

    @Autowired
    @Lazy
    private ConversionService conversionService;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new FromDomainPrimitiveGenericConverter());
        registry.addConverter(new ToDomainPrimitiveGenericConverter());
        registry.addConverterFactory(new StringToDomainPrimitiveConverterFactory<>(conversionService));
    }
}
