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

import com.github.manosbatsis.domainprimitives.core.Sdp4jType;
import com.github.manosbatsis.domainprimitives.test.common.example.network.*;
import com.github.manosbatsis.domainprimitives.test.common.example.network.UriBeanPrimitiveSimple;
import com.github.manosbatsis.domainprimitives.test.common.example.network.UrlRecordPrimitiveSimple;
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

public class SampleData {

    public record ConvertibleConfig<T extends Serializable>(
            T value, Class<? extends Sdp4jType<T>>... domainPrimitiveClasses) {

        public Stream<Convertible<T>> flatten() {
            return Arrays.stream(domainPrimitiveClasses).map(clazz -> toConvertible(value, clazz));
        }

        private Convertible<T> toConvertible(T value, Class<? extends Sdp4jType<T>> clazz) {
            return new Convertible<>(value, clazz);
        }
    }

    public record Convertible<T extends Serializable>(T value, Class<? extends Sdp4jType<?>> domainPrimitiveClass) {}

    public static Stream<Convertible> convertibles() throws MalformedURLException, URISyntaxException {
        return convertibleConfigs().flatMap(it -> it.flatten());
    }

    public static Stream<ConvertibleConfig> convertibleConfigs() throws MalformedURLException, URISyntaxException {
        var randomUuid = UUID.randomUUID();
        var randomUri = new URI("http://%s.com".formatted(UUID.randomUUID().toString()));
        return Stream.of(
                // Network
                new ConvertibleConfig(randomUri, UriRecordPrimitiveSimple.class, UriBeanPrimitiveSimple.class),
                new ConvertibleConfig(randomUri.toURL(), UrlRecordPrimitiveSimple.class, UrlBeanPrimitiveSimple.class),
                // Numbers
                new ConvertibleConfig(
                        BigDecimal.TEN, BigDecimalRecordPrimitiveSimple.class, BigDecimalBeanPrimitiveSimple.class),
                new ConvertibleConfig(
                        BigInteger.TEN, BigIntegerRecordPrimitiveSimple.class, BigIntegerBeanPrimitiveSimple.class),
                new ConvertibleConfig(Byte.MAX_VALUE, ByteRecordPrimitiveSimple.class, ByteBeanPrimitiveSimple.class),
                new ConvertibleConfig(
                        Double.MAX_VALUE, DoubleRecordPrimitiveSimple.class, DoubleBeanPrimitiveSimple.class),
                new ConvertibleConfig(
                        Float.MAX_VALUE, FloatRecordPrimitiveSimple.class, FloatBeanPrimitiveSimple.class),
                new ConvertibleConfig(
                        Integer.MAX_VALUE, IntegerRecordPrimitiveSimple.class, IntegerBeanPrimitiveSimple.class),
                new ConvertibleConfig(Long.MAX_VALUE, LongRecordPrimitiveSimple.class, LongBeanPrimitiveSimple.class),
                new ConvertibleConfig(
                        Short.MAX_VALUE, ShortRecordPrimitiveSimple.class, ShortBeanPrimitiveSimple.class),
                // String-based
                new ConvertibleConfig(
                        randomUuid.toString(), StringRecordPrimitiveSimple.class, StringBeanPrimitiveSimple.class),
                new ConvertibleConfig(randomUuid, UuidRecordPrimitiveSimple.class, UuidBeanPrimitiveSimple.class));
    }
}
