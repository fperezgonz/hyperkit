package solutions.sulfura.hyperkit.utils.test.model.scm.inventory;

import jakarta.persistence.*;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

import java.math.BigDecimal;

@Entity
@Dto
public class Stock {

    @EmbeddedId
    public CompositeKey id;
    public BigDecimal quantity;

    @Embeddable
    @Dto
    public static class CompositeKey {
        @ManyToOne
        public WarehouseLocation warehouseLocation;
        @ManyToOne
        public Product product;
    }

}
