package solutions.sulfura.hyperkit.utils.test.model.contact;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

@Entity
@Dto
public class Country {
    @Id
    public String id;
    public String name;
    public String isoAlpha2;
    public String isoAlpha3;

    public Country() {}
}
