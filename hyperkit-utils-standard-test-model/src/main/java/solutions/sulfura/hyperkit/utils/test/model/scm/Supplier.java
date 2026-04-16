package solutions.sulfura.hyperkit.utils.test.model.scm;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;
import solutions.sulfura.hyperkit.utils.test.model.contact.Address;

@Entity
@Dto
public class Supplier {
    @Id
    public String id;
    public String name;
    public String email;
    public String phone;
    @Embedded
    public Address address;
}
