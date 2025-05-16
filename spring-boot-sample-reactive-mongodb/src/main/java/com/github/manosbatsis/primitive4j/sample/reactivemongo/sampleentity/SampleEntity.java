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
package com.github.manosbatsis.primitive4j.sample.reactivemongo.sampleentity;

import com.github.manosbatsis.primitive4j.core.AbstractMutableDomainPrimitive;
import com.github.manosbatsis.primitive4j.core.GeneratePrimitive;
import com.github.manosbatsis.primitive4j.sample.reactivemongo.sampleentity.network.UriBean;
import com.github.manosbatsis.primitive4j.sample.reactivemongo.sampleentity.network.UriRecord;
import com.github.manosbatsis.primitive4j.sample.reactivemongo.sampleentity.network.UrlBean;
import com.github.manosbatsis.primitive4j.sample.reactivemongo.sampleentity.network.UrlRecord;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;
import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@GeneratePrimitive(name = "BigDecimalBean", valueType = BigDecimal.class, extend = AbstractMutableDomainPrimitive.class)
@GeneratePrimitive(name = "BigDecimalRecord", valueType = BigDecimal.class)
@GeneratePrimitive(name = "BigIntegerBean", valueType = BigInteger.class, extend = AbstractMutableDomainPrimitive.class)
@GeneratePrimitive(name = "BigIntegerRecord", valueType = BigInteger.class)
@GeneratePrimitive(name = "DoubleBean", valueType = Double.class, extend = AbstractMutableDomainPrimitive.class)
@GeneratePrimitive(name = "DoubleRecord", valueType = Double.class)
@GeneratePrimitive(name = "FloatBean", valueType = Float.class, extend = AbstractMutableDomainPrimitive.class)
@GeneratePrimitive(name = "FloatRecord", valueType = Float.class)
@GeneratePrimitive(name = "IntegerBean", valueType = Integer.class, extend = AbstractMutableDomainPrimitive.class)
@GeneratePrimitive(name = "IntegerRecord", valueType = Integer.class)
@GeneratePrimitive(name = "LongBean", valueType = Long.class, extend = AbstractMutableDomainPrimitive.class)
@GeneratePrimitive(name = "LongRecord", valueType = Long.class)
@GeneratePrimitive(name = "ShortBean", valueType = Short.class, extend = AbstractMutableDomainPrimitive.class)
@GeneratePrimitive(name = "ShortRecord", valueType = Short.class)
@GeneratePrimitive(name = "StringBean", valueType = String.class, extend = AbstractMutableDomainPrimitive.class)
@GeneratePrimitive(name = "StringRecord", valueType = String.class)
@GeneratePrimitive(name = "UuidBean", valueType = UUID.class, extend = AbstractMutableDomainPrimitive.class)
@GeneratePrimitive(name = "UuidRecord", valueType = UUID.class)
public class SampleEntity {

    @Id
    private UUID id;

    // Network value types
    private UriBean uriBean;

    private UriRecord uriRecord;

    private UrlBean urlBean;

    private UrlRecord urlRecord;

    // Number value types
    private BigDecimalBean bigDecimalBean;

    private BigDecimalRecord bigDecimalRecord;

    private BigIntegerBean bigIntegerBean;

    private BigIntegerRecord bigIntegerRecord;

    private DoubleBean doubleBean;

    private DoubleRecord doubleRecord;

    private FloatBean floatBean;

    private FloatRecord floatRecord;

    private IntegerBean integerBean;

    private IntegerRecord integerRecord;

    private LongBean longBean;

    private LongRecord longRecord;

    private ShortBean shortBean;

    private ShortRecord shortRecord;
    // String value types
    private StringBean stringBean;

    private StringRecord stringRecord;

    private UuidBean uuidBean;

    private UuidRecord uuidRecord;
}
