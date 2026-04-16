package solutions.sulfura.hyperkit.utils.test.model.scm.shipments;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;
import solutions.sulfura.hyperkit.utils.test.model.scm.Supplier;

@Entity
@Dto
public class Shipment {
    @Id
    public String id;
    public String trackingNumber;
    public String status;

    @Embedded
    public SupplierRef supplierRef;

    @Embeddable
    @Dto
    public static class SupplierRef {
        public String name;
        public String email;
        public String phone;
        public String address;
        @ManyToOne
        public Supplier supplier;
    }

}
