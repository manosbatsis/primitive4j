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
import java.net.URI;
import java.net.URL;

@GenerateSdp4jType(name = "UriBean", valueType = URI.class, extend = AbstractMutableSdp4jType.class)
@GenerateSdp4jType(name = "UriRecord", valueType = URI.class)
@GenerateSdp4jType(name = "UrlBean", valueType = URL.class, extend = AbstractMutableSdp4jType.class)
@GenerateSdp4jType(name = "UrlRecord", valueType = URL.class)
@MappedSuperclass // Trigger JPA code generation
public class SampleNetworkPrimitives {}
