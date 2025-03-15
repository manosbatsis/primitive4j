package com.github.manosbatsis.domainprimitives.sample.order;

import com.github.manosbatsis.domainprimitives.sample.customer.CustomerRef;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.Valid;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id @Valid private OrderId id;
    CustomerRef customerRef;
    private String comments;
}
