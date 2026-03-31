package solutions.sulfura.hyperkit.utils.test.model.scm.inventory;

import jakarta.persistence.*;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Dto
public class Stock implements Serializable {

    @EmbeddedId
    public StockKey id;
    public BigDecimal quantity;

    @Embeddable
    @Dto
    public static class StockKey implements Serializable {
        @ManyToOne
        public WarehouseLocation warehouseLocation;
        @ManyToOne
        public Product product;

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            StockKey stockKey = (StockKey) o;
            return Objects.equals(warehouseLocation, stockKey.warehouseLocation) && Objects.equals(product, stockKey.product);
        }

        @Override
        public int hashCode() {
            return Objects.hash(warehouseLocation, product);
        }

        @Override
        public String toString() {
            return "StockKey{" +
                    "warehouseLocation=" + warehouseLocation +
                    ", product=" + product +
                    '}';
        }
    }

}
