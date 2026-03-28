package solutions.sulfura.hyperkit.utils.test.model.scm.inventory;

import jakarta.persistence.*;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

import java.math.BigDecimal;

@Entity
@Dto
public class Stock {

    @Id
    public CompositeKey id;
    @ManyToOne
    @MapsId("warehouseLocationId")
    public WarehouseLocation location;
    @ManyToOne
    @MapsId("productId")
    public Product product;
    public BigDecimal quantity;

    @Embeddable
    public static class CompositeKey {
        String warehouseLocationId;
        String productId;
    }

}
