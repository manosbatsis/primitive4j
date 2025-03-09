package com.github.manosbatsis.typedidref.sample.order;

import com.github.manosbatsis.typedidref.core.AbstractTypedProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.UUID;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

  public static class OrderRef extends AbstractTypedProperty<Order, String>
      implements Serializable {
    public OrderRef(String value) {
      super(Order.class, value);
    }
  }

  @Id private UUID id;
  private OrderRef ref;
  private String name;
}
