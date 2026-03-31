package solutions.sulfura.hyperkit.utils.test.model.iam;

import jakarta.persistence.Embeddable;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

@Dto
@Embeddable
public class Action {
    public String id;
    public String name;
}
