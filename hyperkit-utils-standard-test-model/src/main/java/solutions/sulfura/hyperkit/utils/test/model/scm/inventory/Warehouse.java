package solutions.sulfura.hyperkit.utils.test.model.scm.inventory;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

import java.util.Set;

@Entity
@Dto
public class Warehouse {
    @Id
    public String id;
    public String name;
    @OneToMany(mappedBy = "warehouse")
    public Set<WarehouseLocation> locations;

}
