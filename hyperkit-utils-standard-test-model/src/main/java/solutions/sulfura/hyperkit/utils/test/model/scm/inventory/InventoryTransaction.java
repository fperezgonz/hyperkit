package solutions.sulfura.hyperkit.utils.test.model.scm.inventory;

import jakarta.persistence.*;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

@Entity
@Dto
public class InventoryTransaction {
    @Id
    public String id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    public Product product;
    public String unitOfMeasure;
    public int quantity;
    @ManyToOne
    @JoinColumn(name = "from_location_id")
    public WarehouseLocation fromLocation;
    @ManyToOne
    @JoinColumn(name = "to_location_id")
    public WarehouseLocation toLocation;
}
