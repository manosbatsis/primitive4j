package com.github.manosbatsis.domainprimitives.sample.customer;

import com.github.manosbatsis.domainprimitives.core.GenerateDomainPrimitive;

import jakarta.persistence.*;
import jakarta.validation.Valid;

import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@GenerateDomainPrimitive(
        name = "CustomerRefTest",
        javaDoc = "A business key type dedicated to Customer entities.",
        valueType = String.class)
public class Customer {

    @Id private UUID id;

    @Valid private CustomerRef ref;

    private String name;
}
