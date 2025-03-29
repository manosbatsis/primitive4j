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

import com.github.manosbatsis.domainprimitives.core.AbstractMutableSdp4jType;
import com.github.manosbatsis.domainprimitives.core.GenerateSdp4jType;
import jakarta.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.math.BigInteger;

@GenerateSdp4jType(name = "BigDecimalBean", valueType = BigDecimal.class, extend = AbstractMutableSdp4jType.class)
@GenerateSdp4jType(name = "BigDecimalRecord", valueType = BigDecimal.class)
@GenerateSdp4jType(name = "BigIntegerBean", valueType = BigInteger.class, extend = AbstractMutableSdp4jType.class)
@GenerateSdp4jType(name = "BigIntegerRecord", valueType = BigInteger.class)
@GenerateSdp4jType(name = "DoubleBean", valueType = Double.class, extend = AbstractMutableSdp4jType.class)
@GenerateSdp4jType(name = "DoubleRecord", valueType = Double.class)
@GenerateSdp4jType(name = "FloatBean", valueType = Float.class, extend = AbstractMutableSdp4jType.class)
@GenerateSdp4jType(name = "FloatRecord", valueType = Float.class)
@GenerateSdp4jType(name = "IntegerBean", valueType = Integer.class, extend = AbstractMutableSdp4jType.class)
@GenerateSdp4jType(name = "IntegerRecord", valueType = Integer.class)
@GenerateSdp4jType(name = "LongBean", valueType = Long.class, extend = AbstractMutableSdp4jType.class)
@GenerateSdp4jType(name = "LongRecord", valueType = Long.class)
@GenerateSdp4jType(name = "ShortBean", valueType = Short.class, extend = AbstractMutableSdp4jType.class)
@GenerateSdp4jType(name = "ShortRecord", valueType = Short.class)
@MappedSuperclass // Trigger JPA code generation
public class SampleNumberPrimitives {}
