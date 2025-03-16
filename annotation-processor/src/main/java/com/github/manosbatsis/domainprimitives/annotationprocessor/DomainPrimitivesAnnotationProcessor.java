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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.manosbatsis.domainprimitives.core.DomainPrimitive;
import com.github.manosbatsis.domainprimitives.core.GenerateDomainPrimitive;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.Diagnostic;
import lombok.Builder;
import lombok.Getter;

/**
 * Annotation processor that handles {@link GenerateDomainPrimitive} annotations
 * to generate implementations of {@link DomainPrimitive}.
 */
public final class DomainPrimitivesAnnotationProcessor extends AbstractProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(GenerateDomainPrimitive.class.getCanonicalName());
    }

    @Getter
    @Builder
    static class DomainPrimitiveGenerationContext {
        GenerateDomainPrimitive annotation;
        TypeElement annotatedTypeElement;
        Element enclosingElement;
        String extendClassName;
        String extendClassSimpleName;
        boolean extendClassIsInterface;
        boolean generateJpa;
        String generatedClassSimpleName;
        String valueClassName;
        String valueTypeSimpleName;
        String packageName;

        public boolean isInvalid(Messager messager) {
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

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        var filer = this.processingEnv.getFiler();
        var messager = this.processingEnv.getMessager();

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(GenerateDomainPrimitive.class);
        for (var element : elements) {

            var ddContext = buildDomainPrimitiveContext((TypeElement) element);
            if (ddContext.isInvalid(messager)) {
                return true;
            }

            var generatedClassJavaDoc = getGeneratedTypeJavaDoc(ddContext.getAnnotation());
            var generatedTypeAnnotations = getGeneratedTypeAnnotations(ddContext.valueTypeSimpleName);
            var generatedImports = getGeneratedImports(ddContext);

            String classDeclStart;
            if (ddContext.extendClassIsInterface) {
                classDeclStart =
                        """
                        %s

                        %s
                        %s
                        public record %s(%s value) implements %s<%s> {

                        """
                                .formatted(
                                        generatedImports.toString(),
                                        generatedClassJavaDoc,
                                        generatedTypeAnnotations,
                                        ddContext.generatedClassSimpleName,
                                        ddContext.valueTypeSimpleName,
                                        ddContext.extendClassSimpleName,
                                        ddContext.valueTypeSimpleName);
            } else {
                classDeclStart =
                        """
                        %s

                        %s
                        %s
                        public class %s extends %s<%s> {

                        %s

                        %s

                        """
                                .formatted(
                                        generatedImports.toString(),
                                        generatedClassJavaDoc,
                                        generatedTypeAnnotations,
                                        ddContext.generatedClassSimpleName,
                                        ddContext.extendClassSimpleName,
                                        ddContext.valueTypeSimpleName,
                                        getGeneratedCreator(
                                                ddContext.generatedClassSimpleName, ddContext.valueTypeSimpleName),
                                        getGeneratedConstructor(
                                                ddContext.generatedClassSimpleName, ddContext.valueTypeSimpleName));
            }

            var classDeclEnd = "\n}\n";

            var classDecl = new StringBuilder();
            classDecl.append(ddContext.getPackageName() == null ? "" : "package " + ddContext.getPackageName() + ";\n");
            classDecl.append(classDeclStart);
            if (ddContext.generateJpa) {
                classDecl.append(getGeneratedJpaAttributeConverter(
                        ddContext.generatedClassSimpleName, ddContext.valueTypeSimpleName));
            }
            classDecl.append(classDeclEnd);

            try {
                var file = filer.createSourceFile(
                        (ddContext.packageName == null ? "" : ddContext.packageName + ".")
                                + ddContext.generatedClassSimpleName,
                        element);
                try (var writer = file.openWriter()) {
                    writer.append(classDecl.toString());
                }
            } catch (IOException e) {
                throw DomainPrimitiveGenerationException.couldNotWriteFile(e);
            }
        }

        return true;
    }

    private DomainPrimitiveGenerationContext buildDomainPrimitiveContext(TypeElement annotatedTypeElement) {
        var annotation = annotatedTypeElement.getAnnotation(GenerateDomainPrimitive.class);
        var jpaMode = annotation.jpaMode();
        boolean hasJpaAnnotation = !annotatedTypeElement.getAnnotationMirrors().stream()
                .filter(annotationMirror ->
                        annotationMirror.getAnnotationType().toString().startsWith("jakarta.persistence"))
                .toList()
                .isEmpty();

        var generateJpa = jpaMode.equals(GenerateDomainPrimitive.FeatureMode.ACTIVE)
                || (jpaMode.equals(GenerateDomainPrimitive.FeatureMode.AUTO) && hasJpaAnnotation);
        var generatedClassSimpleName = getGeneratedClassName(annotation);
        var valueClassName = getValueType(annotation);
        var extendClassName = getExtendedClass(annotation);
        return DomainPrimitiveGenerationContext.builder()
                .generateJpa(generateJpa)
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

    private static String getGeneratedTypeJavaDoc(GenerateDomainPrimitive annotation) {
        var input = annotation.javaDoc().strip();
        var sb = new StringBuilder();
        if (!input.isBlank()) {
            sb.append("/**\n");
            input.lines().forEach(line -> sb.append(" * ").append(line).append("\n"));
            sb.append(" */");
        }
        return sb.toString();
    }

    private static String getGeneratedTypeAnnotations(String valueTypeSimpleName) {
        return """
        @Schema(implementation = %s.class) // Useful for OpenAPI tools like Swagger, SpringDoc etc.
        """
                .formatted(valueTypeSimpleName)
                .strip();
    }

    private String getGeneratedCreator(String generatedClassName, String valueTypeSimpleName) {
        var template =
                """
                    /**
                     * Annotated with @JsonCreator and used by Jackson when deserializing
                     *
                     * @param value the internal, wrapped value
                     * @return a new instance wrapping the given value
                     */
                    @JsonCreator
                    public static %s of(%s value) {
                        return new %s(value);
                    }
                """;
        return removeTrailingNewLines(template.formatted(generatedClassName, valueTypeSimpleName, generatedClassName));
    }

    private String getGeneratedJpaAttributeConverter(String generatedClassName, String valueTypeSimpleName) {
        var template =
                """
                    /**
                     * A JPA converter for {@link %s}
                     */
                    @Converter(autoApply = true)
                    static class %sAttributeConverter
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

    private String getGeneratedConstructor(String generatedClassName, String valueTypeSimpleName) {
        var template =
                """
                    /**
                     * Subtypes of DomainPrimitive must have a single argument constructor
                     * to set internal wrapped value
                     *
                     * @param value the internal, wrapped value
                     */
                    public %s(%s value) {
                        super(value);
                    }
                """;
        return removeTrailingNewLines(template.formatted(generatedClassName, valueTypeSimpleName));
    }

    private static ImportsHelper getGeneratedImports(DomainPrimitiveGenerationContext ddContext) {
        ImportsHelper importsHelper = new ImportsHelper()
                .addImport(DomainPrimitive.class.getCanonicalName())
                .addImport(ddContext.extendClassName)
                .addImport("io.swagger.v3.oas.annotations.media.Schema");
        // Import value type id if needed
        if (!ddContext.valueClassName.startsWith("java.lang")) {
            importsHelper.addImport(ddContext.valueClassName);
        }
        // Import JsonCreator if needed
        if (!ddContext.extendClassIsInterface) {
            importsHelper.addImport(JsonCreator.class);
        }
        if (ddContext.generateJpa) {
            importsHelper
                    .addImport("com.github.manosbatsis.domainprimitives.jpa.DomainPrimitiveAttributeConverter")
                    .addImport("jakarta.persistence.Converter");
        }
        return importsHelper;
    }

    private String getValueType(GenerateDomainPrimitive annotation) {
        String valueType;
        try {
            valueType = annotation.valueType().toString();
        } catch (MirroredTypeException e) {
            valueType = e.getTypeMirror().toString();
        }
        return valueType;
    }

    private String getExtendedClass(GenerateDomainPrimitive annotation) {
        String extendClass;
        try {
            extendClass = annotation.extend().toString();
        } catch (MirroredTypeException e) {
            extendClass = e.getTypeMirror().toString();
        }
        return extendClass;
    }

    private String getGeneratedClassName(GenerateDomainPrimitive annotation) {
        var generatedClassName = annotation.name();

        if (generatedClassName.contains(".")) {
            var split = generatedClassName.split("\\.");
            generatedClassName = split[split.length - 1];
        }
        return generatedClassName;
    }

    private static class ImportsHelper {
        private TreeSet<String> imports = new TreeSet<>();

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
