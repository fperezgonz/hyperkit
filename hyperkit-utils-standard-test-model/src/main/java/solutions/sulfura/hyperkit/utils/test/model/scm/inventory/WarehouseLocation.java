package solutions.sulfura.hyperkit.utils.test.model.scm.inventory;

import jakarta.persistence.*;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

@Entity
@Dto
public class WarehouseLocation {
    @Id
    public String id;
    public String name;
    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    public Warehouse warehouse;
}
