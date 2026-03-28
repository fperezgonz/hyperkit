package solutions.sulfura.hyperkit.utils.test.model.scm.inventory;

import jakarta.persistence.*;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

import java.math.BigDecimal;

@Entity
@Dto
public class Stock {

    @EmbeddedId
    public StockKey id;
    public BigDecimal quantity;

    @Embeddable
    @Dto
    public static class StockKey {
        @ManyToOne
        public WarehouseLocation warehouseLocation;
        @ManyToOne
        public Product product;
    }

}
