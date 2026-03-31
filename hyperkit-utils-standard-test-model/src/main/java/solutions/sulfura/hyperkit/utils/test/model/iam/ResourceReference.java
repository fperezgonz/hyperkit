package solutions.sulfura.hyperkit.utils.test.model.iam;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

@Entity
@Dto
public class ResourceReference {
    @Id
    public String id;
    public String name;
    public String type;
}
