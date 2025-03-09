# jvm-typed-id

Dedicated wrapper types for identifiers and references in Java (Kotlin, Groovy etc.) applications.

## The problem

The downsides of using a common types like `Long`, `String`, `UUID` etc. for identifiers or references can be
painful, with APIs becoming increasingly error-prone as the compiler is unable to protect developers.
Instead, programming errors are caught much later, during automated or manual testing. For example:

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


## The Solution

In Domain-Driven Design (DDD), entity identifiers are essential. Most developers are familiar with using 
simple types but, while these basic types work, they can lead to bugs or confusing code.
That’s where strongly typed identifiers can make your code safer, clearer, and easier to maintain.

Example using simple type (String) references:

```java

public class Order {
    // ...
    private String ref;
    private String customerRef;
    // ...
}

public class Customer {
    // ...
    private String ref;
    // ...
}
```

Same example after introducing typed references:

```java

public class Order {
    // ...
    private OrderId ref;
    private CustomerId customerRef;
    // ...
}

public class Customer {
    // ...
    private CustomerId ref;
    // ...
}
```

## Benefits of Typed References

### Type Safety

Type-safety can now catch mistakes early: method parameters, queries etc. are now checked by the compiler.

### Logical Dependencies Become Physical

In our previous example, the `Order` class has a `customerRef`. The `Customer` has the same information. 
There is clearly a logical relationship between the two classes, but while using simple-typed references they remain 
statically independent.

In his famous book Clean Code, Robert Cecil Martin (colloquially known as Uncle Bob) states that:

> If one module depends upon another, that dependency should be physical, not just logical.

Typed references materialize the logical dependency by creating a physical one. So, if we change the type of the proposal reference, the compiler will stop us if we forget to change the

proposalRef type in the

OnboardingProcess class.

Representation, Validation and Other Logic

A typed reference class is self contained acts as a single point to control representation, e.g. a prefix or other logic.


This includes validation, i.e. when creating a typed reference object we can be sure that it is valid by adding a guard condition in the constructor. We can similarly add other useful logic.

Support Infrastructure

Depending on how abstract we’d like the implementation to be, a Spring Boot starter could provide:

    G
    eneric JPA/Hibernate user type
    G
    eneric Converter for JSON (de)se
    rialization
    (Optional) Code gen
    eration for the reference types themselves
