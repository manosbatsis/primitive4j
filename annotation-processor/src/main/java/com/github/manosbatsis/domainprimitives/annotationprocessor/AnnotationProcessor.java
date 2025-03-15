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

/**
 * Annotation processor that handles GenerateDomainPrimitive annotations.
 */
public final class AnnotationProcessor extends AbstractProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(GenerateDomainPrimitive.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        var filer = this.processingEnv.getFiler();
        var messager = this.processingEnv.getMessager();
        var elementUtils = this.processingEnv.getElementUtils();

        Set<? extends Element> elements =
                roundEnv.getElementsAnnotatedWith(GenerateDomainPrimitive.class);
        for (var element : elements) {
            TypeElement annotatedTypeElement = (TypeElement) element;
            GenerateDomainPrimitive annotation = annotatedTypeElement.getAnnotation(GenerateDomainPrimitive.class);
            String generatedClassSimpleName = getGeneratedClassName(annotation);
            if (isInvalidGeneratedClassName(
                    generatedClassSimpleName, annotatedTypeElement, messager)) {
                return true;
            }

            String valueType = getValueType(annotation);
            String valueTypeSimpleName = valueType.substring(valueType.lastIndexOf('.') + 1);

            var packageName = getGeneratedPackageName(annotatedTypeElement, messager);
            var packageDecl = packageName == null ? "" : "package " + packageName + ";\n";

            var generatedClassJavaDoc = getGeneratedTypeJavaDoc(annotation);
            var generatedTypeAnnotations = getGeneratedTypeAnnotations(valueTypeSimpleName);
            var extendClass = getExtendedClass(annotation);
            var extendClassSimpleName = extendClass.substring(extendClass.lastIndexOf('.') + 1);
            var extendClassIsInterface =
                    processingEnv
                            .getElementUtils()
                            .getTypeElement(extendClass)
                            .getKind()
                            .isInterface();
            var generatedImports =
                    getGeneratedImports(valueType, extendClass, extendClassIsInterface);

            String classDeclStart;
            if (extendClassIsInterface) {
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
                                        generatedClassSimpleName,
                                        valueTypeSimpleName,
                                        extendClassSimpleName,
                                        valueTypeSimpleName);
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
                                        generatedClassSimpleName,
                                        extendClassSimpleName,
                                        valueTypeSimpleName,
                                        getGeneratedCreator(
                                                generatedClassSimpleName, valueTypeSimpleName),
                                        getGeneratedConstructor(
                                                generatedClassSimpleName, valueTypeSimpleName));
            }

            var classDeclEnd = "\n}\n";

            var classDecl = new StringBuilder();
            classDecl.append(packageDecl);
            classDecl.append(classDeclStart);
            classDecl.append(
                    getGeneratedJpaAttributeConverter(
                            generatedClassSimpleName, valueTypeSimpleName));
            classDecl.append(classDeclEnd);

            try {
                var file =
                        filer.createSourceFile(
                                (packageName == null ? "" : packageName + ".")
                                        + generatedClassSimpleName,
                                element);
                try (var writer = file.openWriter()) {
                    writer.append(classDecl.toString());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return true;
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
        return removeTrailingNewLines(
                template.formatted(generatedClassName, valueTypeSimpleName, generatedClassName));
    }

    private String getGeneratedJpaAttributeConverter(
            String generatedClassName, String valueTypeSimpleName) {
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
        return removeTrailingNewLines(
                template.formatted(
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

    private static ImportsHelper getGeneratedImports(
            String valueType, String extendClass, boolean extendClassIsInterface) {
        ImportsHelper importsHelper =
                new ImportsHelper()
                        .addImport(DomainPrimitive.class.getCanonicalName())
                        .addImport(extendClass)
                        .addImport("io.swagger.v3.oas.annotations.media.Schema")
                        .addImport(
                                "com.github.manosbatsis.domainprimitives.jpa.DomainPrimitiveAttributeConverter")
                        .addImport("jakarta.persistence.Converter");
        // Import value type id if needed
        if (!valueType.startsWith("java.lang")) {
            importsHelper.addImport(valueType);
        }
        // Import JsonCreator if needed
        if (!extendClassIsInterface) {
            importsHelper.addImport(JsonCreator.class);
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

    private Object getGeneratedPackageName(TypeElement annotatedTypeElement, Messager messager) {

        var enclosingElement = annotatedTypeElement.getEnclosingElement();
        if (!(enclosingElement instanceof PackageElement packageElement)) {
            messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Domain primitives must be top level classes, not nested within another class",
                    annotatedTypeElement);
            return true;
        }

        String packageName;
        if (packageElement.isUnnamed()) {
            packageName = null;
        } else {
            packageName = packageElement.toString();
        }

        return packageName;
    }

    private String getGeneratedClassName(GenerateDomainPrimitive annotation) {
        var generatedClassName = annotation.name();

        if (generatedClassName.contains(".")) {
            var split = generatedClassName.split("\\.");
            generatedClassName = split[split.length - 1];
        }
        return generatedClassName;
    }

    private boolean isInvalidGeneratedClassName(
            String generatedClassName, Element element, Messager messager) {

        if ("java.lang.Object".equals(generatedClassName)) {
            messager.printMessage(
                    Diagnostic.Kind.ERROR, "A domain primitive must extend a class.", element);
            return true;
        }

        if (generatedClassName.contains("<")) {
            messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "The class a domain primitive extends should not be generic. %s"
                            .formatted(generatedClassName),
                    element);
            return true;
        }

        return false;
    }

    private static class ImportsHelper {
        private TreeSet<String> imports = new TreeSet<>();

        public ImportsHelper addImport(Class<?> clazz) {
            imports.add(clazz.getCanonicalName());
            return this;
        }

        public ImportsHelper addImport(TypeElement element) {
            imports.add(element.getQualifiedName().toString());
            return this;
        }

        public ImportsHelper addImport(String canonicalName) {
            imports.add(canonicalName);
            return this;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            imports.forEach(
                    canonicalName ->
                            sb.append("\n").append("import ").append(canonicalName).append(";"));
            return sb.toString();
        }
    }
}
