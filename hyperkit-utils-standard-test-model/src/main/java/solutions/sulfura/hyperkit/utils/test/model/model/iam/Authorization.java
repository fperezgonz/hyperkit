package solutions.sulfura.hyperkit.utils.test.model.model.iam;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

import java.util.Set;

@Entity
@Dto
public class Authorization {
    @Id
    public String id;
    public String name;
    public Set<ResourceReference> resourceReferences;
    @ManyToOne
    public Role role;
}
