# Primitive4J: Simple Domain Primitives for the JVM ![Maven Central Version](https://img.shields.io/maven-central/v/com.github.manosbatsis.primitive4j/primitive4j-spring-boot-starter) [![CI](https://github.com/manosbatsis/primitive4j/actions/workflows/ci.yml/badge.svg)](https://github.com/manosbatsis/primitive4j/actions/workflows/ci.yml)

Tools to help with simple domain primitives in Java applications, including:

<!-- TOC -->
* [What Are Domain Primitives?](#what-are-domain-primitives)
* [Quick Example](#quick-example)
* [Benefits of Domain Primitives](#benefits-of-domain-primitives)
  * [Type Safety](#type-safety)
  * [Logical Dependencies Become Physical](#logical-dependencies-become-physical)
  * [Representation, Validation and Other Logic](#representation-validation-and-other-logic)
* [What's Included?](#whats-included)
  * [Abstract Types](#abstract-types)
  * [Code Generation](#code-generation)
  * [Spring Converters](#spring-converters)
  * [JPA Conversion](#jpa-conversion)
  * [Spring Boot Starter](#spring-boot-starter)
* [Troubleshooting](#troubleshooting)
  * [Can Simple Domain Primitives be used with JPA (Hibernate/EclipseLink) @Id annotations?](#can-simple-domain-primitives-be-used-with-jpa-hibernateeclipselink-id-annotations)
<!-- TOC -->

## What Are Domain Primitives?

In Domain-Driven Design, domain primitives are similar to value objects. 
Key differences are that you’re requiring the existence of invariants, 
and they must be enforced at the point of creation. 

They are also used to prohibit the use of simple language primitives, or generic types
(including null), as representations of domain model concepts. 

> A value object precise enough in its definition that it, by its mere existence, 
> manifests its validity is called a domain primitive.
> <small>From ["Secure by Design"](https://www.manning.com/books/secure-by-design) by Dan Bergh Johnsson, Daniel Deogun, Daniel Sawano</small>

Examples of simple domain primitives are dedicated typed identifiers, references, units etc. 

## Quick Example

Example before using domain primitives, i.e. using simple type (String) references and language primitives:

```java

public class Order {
    // ...
    private String customerRef;
    private Float packagedWeightGrams;
    // ...
}

public class Customer {
    // ...
    private String ref;
    // ...
}
```

Same example after introducing typed references and other domain primitives:

```java

public class Order {
    // ...
    private CustomerRef customerRef;
    private WeightInGrams packagedWeight;
    // ...
}

public class Customer {
    // ...
    private CustomerRef ref;
    // ...
}
```

## Benefits of Domain Primitives

### Type Safety

Type-safety can now catch mistakes early: method parameters, queries etc. are now checked by the compiler.
This solves a number of problems, including but not limited to:

- Methods with multiple id/reference parameters: the order here is important, and it’s quite easy to make a mistake, e.g.
    ```java
    public void doSomething(String customerRef, String offerRef, String orderRef);
    ```
- Methods that cannot be overloaded, e.g. compare:
    ```java
    public Customer findOneCustomerByRef(String customerRef);
    public Order findOneOrderByRef(String orderRef);
    ```
  with:
    ```java

    public Customer findOneByRef(CustomerRef customerRef);
    public Order findOneByRef(OrderRef orderRef);
    ```
- Complex ORM queries e.g. with ref-based joins of the same datatype can also be error-prone without triggering errors.


### Logical Dependencies Become Physical

In our previous example, the `Order` class has a `customerRef`. The `Customer` has the same information.
There is clearly a logical relationship between the two classes, but while using simple-typed references they remain
statically independent.

> If one module depends upon another, that dependency should be physical, not just logical.
> <small>From [Clean Code](https://www.pearson.com/en-us/subject-catalog/p/clean-code-a-handbook-of-agile-software-craftsmanship/P200000009044/9780136083252)
> by Robert C. Martin</small>

Using domain primitives we can establish typed references and materialize the logical dependency by creating a physical one.


### Representation, Validation and Other Logic

A domain primitive is self-contained and acts as a single point to control representation, e.g. value format or other
validation logic. When creating a domain primitive we can be sure that it is valid by adding a guard condition in the
constructor.

## What's Included?

This repo includes a number of utilities to help you with simple domain primitives.

### Abstract Types

The `primitive4j-core` module comes with `DomainPrimitive`, `AbstractDomainPrimitive` and 
`AbstractMutableDomainPrimitive` which you can implement or extend i.e.:

```java
/** ... */
@Schema(implementation = String.class) // Useful for OpenAPI tools like Swagger, SpringDoc etc.
public record CustomerRef(String value) implements DomainPrimitive<String> {
    // ...
}
```

or:

```java
/** ... */
@Schema(implementation = String.class) // Useful for OpenAPI tools like Swagger, SpringDoc etc.
public class CustomerRef extends AbstractMutableDomainPrimitive<String> {

    /** ... */
    @JsonCreator // Used by Jackson when deserializing
    public CustomerRef(String value) { super(value); }

    // ...
}
```

### Code Generation

The `primitive4j-annotation-processor` module provides (optional) code generation via annotation processing. To start, 
add the dependency using Gradle:

```kotlin
annotationProcessor("com.github.manosbatsis.primitive4j:primitive4j-annotation-processor:1.0.11")
```

If you are using Maven, you need to add the annotation processor as a dependency in yourpom.xmlfile. You also need to 
ensure that the Maven Compiler Plugin is configured to use the annotation processor during the compile phase:

```xml
<dependencies>
  <dependency>
    <groupId>com.github.manosbatsis.primitive4j</groupId>
    <artifactId>primitive4j-annotation-processor</artifactId>
    <version>1.0.11</version>
    <scope>provided</scope>
  </dependency>
</dependencies>

<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-compiler-plugin</artifactId>
      <configuration>
        <annotationProcessorPaths>
          <path>
            <groupId>com.github.manosbatsis.primitive4j</groupId>
            <artifactId>primitive4j-annotation-processor</artifactId>
          </path>
        </annotationProcessorPaths>
      </configuration>
    </plugin>
  </plugins>
</build>
```

You can then add `@GeneratePrimitive` annotations to generate primitives, typically on the class that makes use of them:

```java
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@GeneratePrimitive(
        name = "CustomerRef",
        javaDoc = "A business key type dedicated to Customer entities.",
        valueType = String.class)
@Builder
public class Customer {

    @Id
    @GeneratedValue
    private UUID id;

    @Valid
    @Column(updatable = false, nullable = false, unique = true)
    private CustomerRef ref;

    private String name;
}
```

### Spring Converters

The `primitive4j-spring` and `primitive4j-spring-boot-starter` modules include and autoconfigure Spring 
conversion service components for your simple domain primitives. Between other things, this completes automated 
conversions for controller methods, for example:

```
POST https://foobar/api/orders/1f4250b3-2763-416b-a466-ff4950ba15a7

{
  "customerRef": "CUS-001",
  "packagedWeight": 1200,
}

```

may be handled like:

```java
@PutMapping("{id}")
ResponseEntity<Order> update(@RequestBody OrderDto order, @PathVariable OrderId id) {
    return ResponseEntity.ok(service.update(order, id));
}
```

### JPA Conversion

If JPA is present in your classpath, generated primitives will include a JPA attribute converter. You can follow the 
same pattern for manually created primitives:

```java
/**
 * A business key type dedicated to Customer entities.
 */
@Schema(implementation = String.class) // Useful for OpenAPI tools like Swagger, SpringDoc etc.
public record CustomerRef(String value) implements DomainPrimitive<String> {

    /**
     * A JPA converter for {@link CustomerRef}
     */
    @Converter(autoApply = true)
    public static class CustomerRefAttributeConverter
            extends DomainPrimitiveAttributeConverter<CustomerRef, String> {
        public CustomerRefAttributeConverter() {
            super(CustomerRef.class, String.class);
        }
    }
}
```

### Spring Boot Starter

A Spring Boot starter that autoconfigures everything can be added as a dependency, e.g. with Maven:

```xml
<dependency>
    <groupId>com.github.manosbatsis.primitive4j</groupId>
    <artifactId>primitive4j-spring-boot-starter</artifactId>
    <version>1.0.11</version>
</dependency>
```

or Gradle: 

```kotlin
implementation("com.github.manosbatsis.primitive4j:primitive4j-spring-boot-starter:1.0.11")
```



## Troubleshooting

### Can Simple Domain Primitives be used with JPA (Hibernate/EclipseLink) @Id annotations?

In short, no. Not at the moment at least, as the current implementation is based on JPA Attribute Converters. 
According to the JPA 2.2 spec (10.6):

> Note that Id attributes, version attributes, relationship attributes, and attributes explicitly annotated as 
> Enumerated or Temporal (or designated as such via XML) will not be converted.

Furthermore, section 3.8 of the same spec reads: 

> The conversion of all basic types is supported except for the following: Id attributes (including the
> attributes of embedded ids and derived identities), version attributes, relationship attributes, and 
> attributes explicitly annotated as Enumerated or Temporal or designated as such in the XML 
> descriptor. Auto-apply converters will not be applied to such attributes, and applications that apply 
> converters to such attributes through use of the Convert annotation will not be portable.

If you want to create a simple primitive for a primary key, you might want to consider a user type instead.
If you want more support for this, please create an issue.