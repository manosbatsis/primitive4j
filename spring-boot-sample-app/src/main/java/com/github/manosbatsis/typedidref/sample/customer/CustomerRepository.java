package com.github.manosbatsis.typedidref.sample.customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Customer.CustomerRef> {}
