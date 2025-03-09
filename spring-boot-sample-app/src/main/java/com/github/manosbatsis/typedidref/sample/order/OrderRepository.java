package com.github.manosbatsis.typedidref.sample.order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Order.OrderRef> {}
