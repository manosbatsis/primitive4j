package com.github.manosbatsis.domainprimitives.sample.product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, ProductId> {}
