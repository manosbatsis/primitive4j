package com.github.manosbatsis.domainprimitives.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used by annotation processing to generate domain primitive types.
 *
 * <p>The generated class can also extend another class provided that other class is an extensible
 * subtype of {@link DomainPrimitive} and has a single-arg constructor.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface GenerateDomainPrimitive {

    /**
     * @return The class name of the generated domain primitive.
     */
    String name();

    /**
     * @return The inner value type wrapped by the generated domain primitive.
     */
    Class<?> valueType();

    /**
     * @return A class for the generated abstract class to extend.
     *     <p>The default value, {@link DomainPrimitive}, produces a record type. Using an alternative
     *     like e.g. {@link AbstractDomainPrimitive} will generate a regular, read-only bean class.
     *     <p>Alternatives must have a single generic parameter and a constructor with a single
     *     argument of the same type.
     */
    Class<?> extend() default DomainPrimitive.class;

    /**
     * @return The type-level javadoc to add, including the multiline comment.
     */
    String javaDoc() default "";
}
