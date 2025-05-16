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
package com.github.manosbatsis.primitive4j.annotationprocessor;

import com.github.manosbatsis.primitive4j.core.DomainPrimitive;
import com.github.manosbatsis.primitive4j.core.GeneratePrimitive;
import com.github.manosbatsis.primitive4j.core.GeneratePrimitive.FeatureMode;
import com.github.manosbatsis.primitive4j.core.GeneratePrimitives;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Generated;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.MirroredTypeException;

/**
 * Annotation processor that handles {@link GeneratePrimitive} annotations to generate
 * implementations of {@link DomainPrimitive}.
 * <p>
 * This processor supports incremental compilation, see Gradle's documentation on
 * <a href="https://docs.gradle.org/current/userguide/java_plugin.html#sec:incremental_annotation_processing">
 *     incremental annotation processing
 * </a>.
 */
public final class DomainPrimitiveAnnotationProcessor extends AbstractProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(GeneratePrimitives.class.getCanonicalName(), GeneratePrimitive.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements =
                roundEnv.getElementsAnnotatedWithAny(Set.of(GeneratePrimitive.class, GeneratePrimitives.class));
        for (var element : elements) {
            var annotatedTypeElement = (TypeElement) element;
            var primitive4jTypes = annotatedTypeElement.getAnnotationsByType(GeneratePrimitive.class);
            for (var primitive4jType : primitive4jTypes) {
                if (generateDomainPrimitive(element, primitive4jType, annotatedTypeElement)) return true;
            }
        }
        return true;
    }

    private boolean generateDomainPrimitive(
            Element element, GeneratePrimitive primitive4jType, TypeElement annotatedTypeElement) {

        var filer = this.processingEnv.getFiler();
        var messager = this.processingEnv.getMessager();
        var annotationContext = buildAnnotationContext(annotatedTypeElement, primitive4jType);
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
        classDecl.append(
                annotationContext.getPackageName() == null
                        ? ""
                        : "package " + annotationContext.getPackageName() + ";\n");
        classDecl.append(classDeclStart);
        if (annotationContext.generateJpa) {
            classDecl.append(getGeneratedJpaAttributeConverter(
                    annotationContext.generatedClassSimpleName, annotationContext.valueTypeSimpleName));
        }

        if (annotationContext.generateSpringData) {
            classDecl.append(getGeneratedSpringDataConverters(
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
            throw DomainPrimitiveGenerationException.couldNotWriteFile(e);
        }
        return false;
    }

    public boolean classExists(String canonicalName) {
        return processingEnv.getElementUtils().getTypeElement(canonicalName) != null;
    }

    private DomainPrimitiveContext buildAnnotationContext(
            TypeElement annotatedTypeElement, GeneratePrimitive annotation) {
        boolean hasSpringDataAnnotation = hasSpringDataAnnotation(annotatedTypeElement);
        var generateSpringData = annotation.springDataMode().equals(FeatureMode.ACTIVE)
                || (annotation.springDataMode().equals(FeatureMode.AUTO) && hasSpringDataAnnotation);
        boolean hasJpaAnnotation = hasJpaAnnotation(annotatedTypeElement);
        var generateJpa = annotation.jpaMode().equals(FeatureMode.ACTIVE)
                || (annotation.jpaMode().equals(FeatureMode.AUTO) && hasJpaAnnotation);
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
        return DomainPrimitiveContext.builder()
                .generateSpringData(generateSpringData)
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

    private static boolean hasSpringDataAnnotation(TypeElement annotatedTypeElement) {
        return annotatedTypeElement.getAnnotationMirrors().stream()
                        .anyMatch(annotationMirror ->
                                annotationMirror.getAnnotationType().toString().startsWith("org.springframework.data"))
                || annotatedTypeElement.getEnclosedElements().stream()
                        .anyMatch(enclosedElem -> ElementKind.FIELD == enclosedElem.getKind()
                                && enclosedElem.getAnnotationMirrors().stream()
                                        .anyMatch(annotationMirror -> annotationMirror
                                                .getAnnotationType()
                                                .toString()
                                                .startsWith("org.springframework.data")));
    }

    private static boolean hasJpaAnnotation(TypeElement annotatedTypeElement) {
        return annotatedTypeElement.getAnnotationMirrors().stream()
                .anyMatch(annotationMirror ->
                        annotationMirror.getAnnotationType().toString().startsWith("jakarta.persistence"));
    }

    private static String getGeneratedTypeJavaDoc(GeneratePrimitive annotation) {
        var input = annotation.javaDoc().strip();
        var sb = new StringBuilder();
        if (!input.isBlank()) {
            sb.append("/**\n");
            input.lines().forEach(line -> sb.append(" * ").append(line).append("\n"));
            sb.append(" */");
        }
        return sb.toString();
    }

    private static String getGeneratedTypeAnnotations(DomainPrimitiveContext annotationContext) {
        StringBuilder sb = new StringBuilder("%n@Generated(%n    value=\"%s\", %n    date=\"%s\", %n    comments=\"%s\")"
                .formatted(
                        DomainPrimitiveAnnotationProcessor.class.getCanonicalName(),
                        ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT),
                        "Annotated element: %s"
                                .formatted(annotationContext
                                        .annotatedTypeElement
                                        .getQualifiedName()
                                        .toString())));
        if (annotationContext.generateOpenApi) {
            sb.append("%n@Schema(implementation = %s.class) // Useful for OpenAPI tools like Swagger, SpringDoc etc."
                    .formatted(annotationContext.valueTypeSimpleName));
        }
        return sb.toString();
    }

    private String getGeneratedJpaAttributeConverter(String generatedClassName, String valueTypeSimpleName) {
        var template =
                """
                    /** A JPA converter for {@link %s} */
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

    private String getGeneratedSpringDataConverters(String generatedClassName, String valueTypeSimpleName) {
        var templateReadingConverter =
                """
                    /** A Spring Data reading converter for {@link %s} */
                    @ReadingConverter
                    public class %sTo%sConverter implements Converter<%s, %s> {
                        @Override
                        public %s convert(%s source) {
                            return new %s(source);
                        }
                    }
                """
                        .formatted(
                                generatedClassName,
                                valueTypeSimpleName,
                                generatedClassName,
                                valueTypeSimpleName,
                                generatedClassName,
                                generatedClassName,
                                valueTypeSimpleName,
                                generatedClassName);
        var templateWritingConverter =
                """
                    /** A Spring Data writing converter for {@link %s} */
                    @WritingConverter
                    public class %sTo%sConverter implements Converter<%s, %s> {
                        @Override
                        public %s convert(%s source) {
                            return source.value();
                        }
                    }
                """
                        .formatted(
                                valueTypeSimpleName,
                                generatedClassName,
                                valueTypeSimpleName,
                                generatedClassName,
                                valueTypeSimpleName,
                                valueTypeSimpleName,
                                generatedClassName);
        return removeTrailingNewLines(templateReadingConverter + "\n" + templateWritingConverter);
    }

    private String removeTrailingNewLines(String value) {
        return value.replaceAll("[\n\r]$", "");
    }

    private String getGeneratedConstructor(DomainPrimitiveContext domainPrimitiveContext) {
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
                domainPrimitiveContext.generateJackson
                        ? "\n    @JsonCreator // Used by Jackson when deserializing"
                        : "",
                domainPrimitiveContext.getGeneratedClassSimpleName(),
                domainPrimitiveContext.getValueTypeSimpleName()));
    }

    private static ImportsHelper getGeneratedImports(DomainPrimitiveContext domainPrimitiveContext) {
        ImportsHelper importsHelper = new ImportsHelper()
                .addImport(Generated.class.getCanonicalName())
                .addImport(DomainPrimitive.class.getCanonicalName())
                .addImport(domainPrimitiveContext.extendClassName);
        if (domainPrimitiveContext.generateOpenApi) {
            importsHelper.addImport("io.swagger.v3.oas.annotations.media.Schema");
        }
        // Import value type id if needed
        if (!domainPrimitiveContext.valueClassName.startsWith("java.lang")) {
            importsHelper.addImport(domainPrimitiveContext.valueClassName);
        }
        // Import JsonCreator if needed
        if (!domainPrimitiveContext.extendClassIsInterface && domainPrimitiveContext.generateJackson) {
            importsHelper.addImport("com.fasterxml.jackson.annotation.JsonCreator");
        }
        // Import JPA attribute converter if needed
        if (domainPrimitiveContext.generateJpa) {
            importsHelper
                    .addImport("com.github.manosbatsis.primitive4j.jpa.DomainPrimitiveAttributeConverter")
                    .addImport("jakarta.persistence.Converter");
        }
        // Import Spring (Data) converter if needed
        if (domainPrimitiveContext.generateSpringData) {
            importsHelper
                    .addImport("org.springframework.core.convert.converter.Converter")
                    .addImport("org.springframework.data.convert.ReadingConverter")
                    .addImport("org.springframework.data.convert.WritingConverter");
        }
        return importsHelper;
    }

    private String getValueType(GeneratePrimitive annotation) {
        String valueType;
        try {
            valueType = annotation.valueType().toString();
        } catch (MirroredTypeException e) {
            valueType = e.getTypeMirror().toString();
        }
        return valueType;
    }

    private String getExtendedClass(GeneratePrimitive annotation) {
        String extendClass;
        try {
            extendClass = annotation.extend().toString();
        } catch (MirroredTypeException e) {
            extendClass = e.getTypeMirror().toString();
        }
        return extendClass;
    }

    private String getGeneratedClassName(GeneratePrimitive annotation) {
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
