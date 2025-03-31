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

import com.github.manosbatsis.domainprimitives.core.GenerateSdp4jType;
import com.github.manosbatsis.domainprimitives.core.GenerateSdp4jType.FeatureMode;
import com.github.manosbatsis.domainprimitives.core.GenerateSdp4jTypes;
import com.github.manosbatsis.domainprimitives.core.Sdp4jType;
import java.io.IOException;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.MirroredTypeException;

/**
 * Annotation processor that handles {@link GenerateSdp4jType} annotations to generate
 * implementations of {@link Sdp4jType}.
 * <p>
 * This processor supports incremental compilation, see Gradle's documentation on
 * <a href="https://docs.gradle.org/current/userguide/java_plugin.html#sec:incremental_annotation_processing">
 *     incremental annotation processing
 * </a>.
 */
public final class Sdp4jTypeAnnotationProcessor extends AbstractProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(GenerateSdp4jTypes.class.getCanonicalName(), GenerateSdp4jType.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements =
                roundEnv.getElementsAnnotatedWithAny(Set.of(GenerateSdp4jType.class, GenerateSdp4jTypes.class));
        for (var element : elements) {
            var annotatedTypeElement = (TypeElement) element;
            var sdp4jTypes = annotatedTypeElement.getAnnotationsByType(GenerateSdp4jType.class);
            for (var sdp4jType : sdp4jTypes) {
                if (generateSdp4j(element, sdp4jType, annotatedTypeElement)) return true;
            }
        }
        return true;
    }

    private boolean generateSdp4j(Element element, GenerateSdp4jType sdp4jType, TypeElement annotatedTypeElement) {

        var filer = this.processingEnv.getFiler();
        var messager = this.processingEnv.getMessager();
        var annotationContext = buildAnnotationContext(annotatedTypeElement, sdp4jType);
        if (annotationContext.isInvalid(messager)) {
            return true;
        }

        var generatedClassJavaDoc = getGeneratedTypeJavaDoc(annotationContext.getAnnotation());
        var generatedTypeAnnotations = getGeneratedTypeAnnotations(annotationContext);
        var generatedImports = getGeneratedImports(annotationContext);

        String classDeclStart;
        if (annotationContext.extendClassIsInterface) {
            classDeclStart =
                    """
                    %s

                    %s%s
                    public record %s(%s value) implements %s<%s> {

                    """
                            .formatted(
                                    generatedImports.toString(),
                                    generatedClassJavaDoc,
                                    generatedTypeAnnotations,
                                    annotationContext.generatedClassSimpleName,
                                    annotationContext.valueTypeSimpleName,
                                    annotationContext.extendClassSimpleName,
                                    annotationContext.valueTypeSimpleName);
        } else {
            classDeclStart =
                    """
                    %s

                    %s
                    %s
                    public class %s extends %s<%s> {

                    %s

                    """
                            .formatted(
                                    generatedImports.toString(),
                                    generatedClassJavaDoc,
                                    generatedTypeAnnotations,
                                    annotationContext.generatedClassSimpleName,
                                    annotationContext.extendClassSimpleName,
                                    annotationContext.valueTypeSimpleName,
                                    getGeneratedConstructor(annotationContext));
        }

        var classDeclEnd = "\n}\n";

        var classDecl = new StringBuilder();
        classDecl.append("// Generated by %s at %s, %n// based on %s%n"
                .formatted(
                        getClass().getSimpleName(),
                        OffsetDateTime.now(Clock.systemUTC()).toString(),
                        annotationContext
                                .annotatedTypeElement
                                .getQualifiedName()
                                .toString()));
        classDecl.append(
                annotationContext.getPackageName() == null
                        ? ""
                        : "package " + annotationContext.getPackageName() + ";\n");
        classDecl.append(classDeclStart);
        if (annotationContext.generateJpa) {
            classDecl.append(getGeneratedJpaAttributeConverter(
                    annotationContext.generatedClassSimpleName, annotationContext.valueTypeSimpleName));
        }
        classDecl.append(classDeclEnd);

        try {
            var file = filer.createSourceFile(
                    (annotationContext.packageName == null ? "" : annotationContext.packageName + ".")
                            + annotationContext.generatedClassSimpleName,
                    element);
            try (var writer = file.openWriter()) {
                writer.append(classDecl.toString());
            }
        } catch (IOException e) {
            throw Sdp4jTypeGenerationException.couldNotWriteFile(e);
        }
        return false;
    }

    public boolean classExists(String canonicalName) {
        return processingEnv.getElementUtils().getTypeElement(canonicalName) != null;
    }

    private Sdp4jTypeContext buildAnnotationContext(TypeElement annotatedTypeElement, GenerateSdp4jType annotation) {
        var jpaMode = annotation.jpaMode();
        boolean hasJpaAnnotation = !annotatedTypeElement.getAnnotationMirrors().stream()
                .filter(annotationMirror ->
                        annotationMirror.getAnnotationType().toString().startsWith("jakarta.persistence"))
                .toList()
                .isEmpty();
        var generateJpa = jpaMode.equals(FeatureMode.ACTIVE) || (jpaMode.equals(FeatureMode.AUTO) && hasJpaAnnotation);
        var jacksonMode = annotation.jacksonMode();
        var isJacksonPresent = classExists("com.fasterxml.jackson.annotation.JsonCreator");
        var generateJackson =
                jacksonMode.equals(FeatureMode.ACTIVE) || jacksonMode.equals(FeatureMode.AUTO) && isJacksonPresent;

        var openApiMode = annotation.openApiMode();
        var isOpenApiPresent = classExists("io.swagger.v3.oas.annotations.media.Schema");
        var generateOpenApi =
                openApiMode.equals(FeatureMode.ACTIVE) || openApiMode.equals(FeatureMode.AUTO) && isOpenApiPresent;

        var generatedClassSimpleName = getGeneratedClassName(annotation);
        var valueClassName = getValueType(annotation);
        var extendClassName = getExtendedClass(annotation);
        return Sdp4jTypeContext.builder()
                .generateJpa(generateJpa)
                .generateJackson(generateJackson)
                .generateOpenApi(generateOpenApi)
                .annotation(annotation)
                .enclosingElement(annotatedTypeElement.getEnclosingElement())
                .annotatedTypeElement(annotatedTypeElement)
                .generatedClassSimpleName(generatedClassSimpleName)
                .valueClassName(valueClassName)
                .valueTypeSimpleName(valueClassName.substring(valueClassName.lastIndexOf('.') + 1))
                .extendClassName(extendClassName)
                .extendClassSimpleName(extendClassName.substring(extendClassName.lastIndexOf('.') + 1))
                .extendClassIsInterface(processingEnv
                        .getElementUtils()
                        .getTypeElement(extendClassName)
                        .getKind()
                        .isInterface())
                .build();
    }

    private static String getGeneratedTypeJavaDoc(GenerateSdp4jType annotation) {
        var input = annotation.javaDoc().strip();
        var sb = new StringBuilder();
        if (!input.isBlank()) {
            sb.append("/**\n");
            input.lines().forEach(line -> sb.append(" * ").append(line).append("\n"));
            sb.append(" */");
        }
        return sb.toString();
    }

    private static String getGeneratedTypeAnnotations(Sdp4jTypeContext sdp4jTypeContext) {
        if (sdp4jTypeContext.generateOpenApi) {
            return "@Schema(implementation = %s.class) // Useful for OpenAPI tools like Swagger, SpringDoc etc."
                    .formatted(sdp4jTypeContext.valueTypeSimpleName);
        } else {
            return "";
        }
    }

    private String getGeneratedJpaAttributeConverter(String generatedClassName, String valueTypeSimpleName) {
        var template =
                """
                    /**
                     * A JPA converter for {@link %s}
                     */
                    @Converter(autoApply = true)
                    public static class %sAttributeConverter
                            extends DomainPrimitiveAttributeConverter<%s, %s> {
                        public %sAttributeConverter() {
                            super(%s.class, %s.class);
                        }
                    }
                """;
        return removeTrailingNewLines(template.formatted(
                generatedClassName,
                generatedClassName,
                generatedClassName,
                valueTypeSimpleName,
                generatedClassName,
                generatedClassName,
                valueTypeSimpleName));
    }

    private String removeTrailingNewLines(String value) {
        return value.replaceAll("[\n\r]$", "");
    }

    private String getGeneratedConstructor(Sdp4jTypeContext sdp4jTypeContext) {
        var template =
                """
                    /**
                     * Subtypes of DomainPrimitive must have a single argument constructor
                     * to set internal wrapped value.
                     *
                     * @param value the internal, wrapped value
                     */%s
                    public %s(%s value) {
                        super(value);
                    }
                """;
        return removeTrailingNewLines(template.formatted(
                sdp4jTypeContext.generateJackson ? "\n    @JsonCreator // Used by Jackson when deserializing" : "",
                sdp4jTypeContext.getGeneratedClassSimpleName(),
                sdp4jTypeContext.getValueTypeSimpleName()));
    }

    private static ImportsHelper getGeneratedImports(Sdp4jTypeContext sdp4jTypeContext) {
        ImportsHelper importsHelper = new ImportsHelper()
                .addImport(Sdp4jType.class.getCanonicalName())
                .addImport(sdp4jTypeContext.extendClassName);
        if (sdp4jTypeContext.generateOpenApi) {
            importsHelper.addImport("io.swagger.v3.oas.annotations.media.Schema");
        }
        // Import value type id if needed
        if (!sdp4jTypeContext.valueClassName.startsWith("java.lang")) {
            importsHelper.addImport(sdp4jTypeContext.valueClassName);
        }
        // Import JsonCreator if needed
        if (!sdp4jTypeContext.extendClassIsInterface && sdp4jTypeContext.generateJackson) {
            importsHelper.addImport("com.fasterxml.jackson.annotation.JsonCreator");
        }
        if (sdp4jTypeContext.generateJpa) {
            importsHelper
                    .addImport("com.github.manosbatsis.domainprimitives.jpa.DomainPrimitiveAttributeConverter")
                    .addImport("jakarta.persistence.Converter");
        }
        return importsHelper;
    }

    private String getValueType(GenerateSdp4jType annotation) {
        String valueType;
        try {
            valueType = annotation.valueType().toString();
        } catch (MirroredTypeException e) {
            valueType = e.getTypeMirror().toString();
        }
        return valueType;
    }

    private String getExtendedClass(GenerateSdp4jType annotation) {
        String extendClass;
        try {
            extendClass = annotation.extend().toString();
        } catch (MirroredTypeException e) {
            extendClass = e.getTypeMirror().toString();
        }
        return extendClass;
    }

    private String getGeneratedClassName(GenerateSdp4jType annotation) {
        var generatedClassName = annotation.name();

        if (generatedClassName.contains(".")) {
            var split = generatedClassName.split("\\.");
            generatedClassName = split[split.length - 1];
        }
        return generatedClassName;
    }

    private static class ImportsHelper {
        private final TreeSet<String> imports = new TreeSet<>();

        public ImportsHelper addImport(Class<?> clazz) {
            imports.add(clazz.getCanonicalName());
            return this;
        }

        public ImportsHelper addImport(String canonicalName) {
            imports.add(canonicalName);
            return this;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            imports.forEach(canonicalName ->
                    sb.append("\n").append("import ").append(canonicalName).append(";"));
            return sb.toString();
        }
    }
}
