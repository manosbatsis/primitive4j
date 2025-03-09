package com.github.manosbatsis.typedidref.sample.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.manosbatsis.typedidref.core.AbstractTypedProperty;
import com.github.manosbatsis.typedidref.jpa.TypedPropertyAttributeConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

  @Schema(implementation = String.class)
  public static class CustomerRef extends AbstractTypedProperty<Customer, String> {

    @JsonCreator
    public static CustomerRef of(String value) {
      return new CustomerRef(value);
    }

    public CustomerRef(String value) {
      super(Customer.class, value);
    }

    @Converter(autoApply = true)
    static class CustomerRefConverter extends TypedPropertyAttributeConverter<CustomerRef, String> {
      public CustomerRefConverter() {
        super(CustomerRef.class);
      }
    }
  }

  @Id private UUID id;

  private CustomerRef ref;

  private String name;
}
