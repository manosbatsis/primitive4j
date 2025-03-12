package com.github.manosbatsis.domainprimitives.sample.order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, OrderId> {}
