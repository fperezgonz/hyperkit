package solutions.sulfura.hyperkit.utils.test.model.model.iam;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

import java.util.Set;

@Entity
@Dto
public class Role {
    @Id
    public String id;
    public String name;
    @ElementCollection
    public Set<Action> actions;
}
