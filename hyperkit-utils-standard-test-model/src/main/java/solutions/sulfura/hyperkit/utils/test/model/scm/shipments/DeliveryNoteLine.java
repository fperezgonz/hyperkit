package solutions.sulfura.hyperkit.utils.test.model.scm.shipments;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

@Entity
@Dto
public class DeliveryNoteLine {
    @Id
    @GeneratedValue
    public String id;
    public String code;
    public String concept;
    @ManyToOne
    public DeliveryNote deliveryNote;

}
