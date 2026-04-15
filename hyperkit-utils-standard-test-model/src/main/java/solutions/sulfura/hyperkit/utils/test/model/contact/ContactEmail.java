package solutions.sulfura.hyperkit.utils.test.model.contact;

import jakarta.persistence.*;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

@Entity
@Dto
public class ContactEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public String id;
    public String email;
    @ManyToOne
    public Contact contact;
}
