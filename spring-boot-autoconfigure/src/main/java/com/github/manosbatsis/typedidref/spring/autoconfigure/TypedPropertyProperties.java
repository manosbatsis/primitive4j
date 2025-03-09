package com.github.manosbatsis.typedidref.spring.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "manosbatsis.typedidref")
public record TypedPropertyProperties() {}
