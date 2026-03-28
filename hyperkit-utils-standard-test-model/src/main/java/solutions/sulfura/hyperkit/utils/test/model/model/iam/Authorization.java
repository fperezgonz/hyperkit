package solutions.sulfura.hyperkit.utils.test.model.model.iam;


import jakarta.persistence.*;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

import java.util.Set;

@Entity
@Dto
public class Authorization {
    @Id
    public String id;
    public String name;
    @ManyToMany
    public Set<ResourceReference> resourceReferences;
    @ManyToOne
    public Role role;
}
