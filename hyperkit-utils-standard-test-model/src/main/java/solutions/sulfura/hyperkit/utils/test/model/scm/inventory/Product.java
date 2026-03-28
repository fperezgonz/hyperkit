package solutions.sulfura.hyperkit.utils.test.model.scm.inventory;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

@Entity
@Dto
public class Product {
    @Id
    public String id;
    public String sku;
    public String name;
}
