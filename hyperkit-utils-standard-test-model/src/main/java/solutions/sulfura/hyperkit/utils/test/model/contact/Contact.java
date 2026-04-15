package solutions.sulfura.hyperkit.utils.test.model.contact;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

import java.util.Set;

@Entity
@Dto
public class Contact {
    @Id
    public String id;
    public String name;
    @OneToMany(mappedBy = "contact")
    public Set<ContactEmail> emails;
    @OneToMany(mappedBy = "contact")
    public Set<ContactPhone> phones;
    @Embedded
    public Address address;
}
