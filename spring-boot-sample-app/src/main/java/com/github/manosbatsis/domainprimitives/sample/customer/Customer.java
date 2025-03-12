package com.github.manosbatsis.domainprimitives.sample.customer;

import jakarta.persistence.*;
import jakarta.validation.Valid;

import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id private UUID id;

    @Valid private CustomerRef ref;

    private String name;
}
