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
package com.github.manosbatsis.domainprimitives.test.common.example;

import com.github.manosbatsis.domainprimitives.core.DomainPrimitive;
import com.github.manosbatsis.domainprimitives.test.common.example.network.UriBeanPrimitive;
import com.github.manosbatsis.domainprimitives.test.common.example.network.UriRecordPrimitive;
import com.github.manosbatsis.domainprimitives.test.common.example.network.UrlBeanPrimitive;
import com.github.manosbatsis.domainprimitives.test.common.example.network.UrlRecordPrimitive;
import com.github.manosbatsis.domainprimitives.test.common.example.numbers.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SampleData {

    public record ConvertibleConfig(
            Serializable value, Class<? extends DomainPrimitive<?>>... domainPrimitiveClasses) {}

    public record Convertible<T>(T value, Class<? extends DomainPrimitive<?>> domainPrimitiveClass) {
        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("value", value)
                    .append("domainPrimitiveClass", domainPrimitiveClass)
                    .toString();
        }
    }

    public static Stream<Convertible<? extends Serializable>> convertibles()
            throws MalformedURLException, URISyntaxException {
        return convertibleConfigs().flatMap(config -> Arrays.stream(config.domainPrimitiveClasses)
                .map(clazz -> new Convertible<>(config.value, clazz)));
    }

    public static Stream<ConvertibleConfig> convertibleConfigs() throws MalformedURLException, URISyntaxException {
        var randomUuid = UUID.randomUUID();
        var randomUri = new URI("http://%s.com".formatted(UUID.randomUUID().toString()));
        return Stream.of(
                // Network
                new ConvertibleConfig(randomUri, UriRecordPrimitive.class, UriBeanPrimitive.class),
                new ConvertibleConfig(randomUri.toURL(), UrlRecordPrimitive.class, UrlBeanPrimitive.class),
                // Numbers
                new ConvertibleConfig(BigDecimal.TEN, BigDecimalRecordPrimitive.class, BigDecimalBeanPrimitive.class),
                new ConvertibleConfig(BigInteger.TEN, BigIntegerRecordPrimitive.class, BigIntegerBeanPrimitive.class),
                new ConvertibleConfig(Byte.MAX_VALUE, ByteRecordPrimitive.class, ByteBeanPrimitive.class),
                new ConvertibleConfig(Double.MAX_VALUE, DoubleRecordPrimitive.class, DoubleBeanPrimitive.class),
                new ConvertibleConfig(Float.MAX_VALUE, FloatRecordPrimitive.class, FloatBeanPrimitive.class),
                new ConvertibleConfig(Integer.MAX_VALUE, IntegerRecordPrimitive.class, IntegerBeanPrimitive.class),
                new ConvertibleConfig(Long.MAX_VALUE, LongRecordPrimitive.class, LongBeanPrimitive.class),
                new ConvertibleConfig(Short.MAX_VALUE, ShortRecordPrimitive.class, ShortBeanPrimitive.class),
                // String-based
                new ConvertibleConfig(randomUuid.toString(), StringRecordPrimitive.class, StringBeanPrimitive.class),
                new ConvertibleConfig(randomUuid, UuidRecordPrimitive.class, UuidBeanPrimitive.class));
    }
}
