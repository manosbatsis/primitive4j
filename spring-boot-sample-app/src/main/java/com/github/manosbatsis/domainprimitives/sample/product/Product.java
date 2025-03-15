package com.github.manosbatsis.domainprimitives.sample.product;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.Valid;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id @Valid private ProductId id;
    WeightInGrams packagedWeightGrams;
    private String description;
}
