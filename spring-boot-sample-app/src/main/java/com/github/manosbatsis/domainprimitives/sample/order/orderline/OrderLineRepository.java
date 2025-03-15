package com.github.manosbatsis.domainprimitives.sample.order.orderline;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderLine, OrderLineId> {}
