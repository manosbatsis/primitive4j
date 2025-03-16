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
