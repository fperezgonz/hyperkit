package solutions.sulfura.hyperkit.utils.test.model.scm.shipments;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

import java.util.Set;

@Entity
@Dto
public class DeliveryNote {
    @Id
    @GeneratedValue
    public String id;
    public String year;
    public String code;
    @OneToMany(mappedBy = "deliveryNote")
    public Set<DeliveryNoteLine> deliveryNoteLines;
}
