package com.github.manosbatsis.domainprimitives.spring.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "manosbatsis.domainprimitives")
public record DomainPrimitivesProperties() {

}
