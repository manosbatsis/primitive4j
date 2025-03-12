package com.github.manosbatsis.domainprimitives.sample.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, CustomerRef> {

    Optional<Customer> findOneByRef(CustomerRef ref);
}
