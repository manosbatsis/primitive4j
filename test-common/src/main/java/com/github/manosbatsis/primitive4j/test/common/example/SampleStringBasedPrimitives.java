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
package com.github.manosbatsis.primitive4j.test.common.example;

import com.github.manosbatsis.primitive4j.core.AbstractMutableDomainPrimitive;
import com.github.manosbatsis.primitive4j.core.GeneratePrimitive;
import jakarta.persistence.MappedSuperclass;
import java.util.UUID;

@GeneratePrimitive(name = "StringBean", valueType = String.class, extend = AbstractMutableDomainPrimitive.class)
@GeneratePrimitive(name = "StringRecord", valueType = String.class)
@GeneratePrimitive(name = "UuidBean", valueType = UUID.class, extend = AbstractMutableDomainPrimitive.class)
@GeneratePrimitive(name = "UuidRecord", valueType = UUID.class)
@MappedSuperclass // Trigger JPA code generation
public class SampleStringBasedPrimitives {}
