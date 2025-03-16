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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * (Optional) Used by annotation processing to generate domain primitive types.
 *
 * <p>The generated class can also extend another class provided that other class is an extensible
 * subtype of {@link DomainPrimitive} and has a single-arg constructor.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface GenerateDomainPrimitive {

    /** The class name of the generated domain primitive. */
    String name();

    /** The inner value type wrapped by the generated domain primitive. */
    Class<?> valueType();

    /**
     * A class for the generated interface or abstract class to implement or extend respectively.
     *
     * <p>Using the default value of {@link DomainPrimitive} or a sub-interface produces a record type.
     * A concrete type like{@link AbstractDomainPrimitive} will produce a regular, read-only POJO.
     *
     * <p>Concrete types must have a single generic parameter and a constructor with a single argument
     * of the same type.
     */
    Class<?> extend() default DomainPrimitive.class;

    /** The type-level javadoc to add */
    String javaDoc() default "";

    /**
     * Whether to generate a JPA converter for this domain primitive. Default is AUTO, in which case a
     * converter will be generated if the annotated element is also annotated with a <code>
     * jakarta.persistence</code> annotation, e.g. <code>Entity</code>, <code>MappedSuperclass</code>
     * or <code>Embeddable</code>.
     */
    FeatureMode jpaMode() default FeatureMode.AUTO;

    enum FeatureMode {
        ACTIVE,
        INACTIVE,
        AUTO;
    }
}
