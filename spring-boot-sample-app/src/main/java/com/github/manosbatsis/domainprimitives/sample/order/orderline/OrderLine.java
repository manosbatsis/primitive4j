package com.github.manosbatsis.domainprimitives.sample.order.orderline;

import com.github.manosbatsis.domainprimitives.sample.order.Order;
import com.github.manosbatsis.domainprimitives.sample.product.Product;

import jakarta.persistence.*;
import jakarta.validation.Valid;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderLine {
    @Id @Valid private OrderLineId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    Product product;
}
