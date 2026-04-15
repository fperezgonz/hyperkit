package solutions.sulfura.hyperkit.utils.test.model.contact;

import jakarta.persistence.*;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

@Entity
@Dto
public class ContactPhone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public String id;
    public String number;
    @ManyToOne
    public Contact contact;
}
