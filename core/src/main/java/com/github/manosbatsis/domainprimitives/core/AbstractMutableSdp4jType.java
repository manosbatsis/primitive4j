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
package com.github.manosbatsis.domainprimitives.core;

import java.io.Serializable;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @param <I> the primitive wrapped value
 */
@Slf4j
@Getter
@Setter
@EqualsAndHashCode
public abstract class AbstractMutableSdp4jType<I extends Serializable> implements Sdp4jType<I> {

    private I value;

    public AbstractMutableSdp4jType(I value) {
        this.value = value;
        log.info("Initialized with {} value: {}", value.getClass().getSimpleName(), value);
    }

    @Override
    public I value() {
        return getValue();
    }
}
