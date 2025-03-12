package com.github.manosbatsis.typedidref.sample.order;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id private OrderId id;
    private OffsetDateTime orderDateTime;
}
