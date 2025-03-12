package com.github.manosbatsis.typedidref.sample.customer;

import jakarta.persistence.*;

import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id private UUID id;

    private CustomerRef ref;

    private String name;
}
