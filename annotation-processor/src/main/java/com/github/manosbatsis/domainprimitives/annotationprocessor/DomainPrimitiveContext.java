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
package com.github.manosbatsis.domainprimitives.annotationprocessor;

import com.github.manosbatsis.domainprimitives.core.GeneratePrimitive;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
class DomainPrimitiveContext {
    GeneratePrimitive annotation;
    TypeElement annotatedTypeElement;
    Element enclosingElement;
    String extendClassName;
    String extendClassSimpleName;
    boolean extendClassIsInterface;
    boolean generateJpa;
    boolean generateJackson;
    boolean generateOpenApi;
    String generatedClassSimpleName;
    String valueClassName;
    String valueTypeSimpleName;
    String packageName;

    boolean isInvalid(Messager messager) {
        if (isInvalidGeneratedClassName(generatedClassSimpleName, annotatedTypeElement, messager)) {
            return true;
        }
        if (!(enclosingElement instanceof PackageElement packageElement)) {
            messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Domain primitives must be top level classes, not nested within another" + " class",
                    annotatedTypeElement);
            return true;
        }
        if (packageElement.isUnnamed()) {
            packageName = null;
        } else {
            packageName = packageElement.toString();
        }
        return false;
    }

    private boolean isInvalidGeneratedClassName(String generatedClassName, Element element, Messager messager) {

        if ("java.lang.Object".equals(generatedClassName)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "A domain primitive must extend a class.", element);
            return true;
        }

        if (generatedClassName.contains("<")) {
            messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "The class a domain primitive extends should not be generic. %s".formatted(generatedClassName),
                    element);
            return true;
        }

        return false;
    }
}
