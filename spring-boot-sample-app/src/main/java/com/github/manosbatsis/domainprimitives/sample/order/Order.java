package com.github.manosbatsis.domainprimitives.sample.order;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id private OrderId id;
    private String comments;
}
