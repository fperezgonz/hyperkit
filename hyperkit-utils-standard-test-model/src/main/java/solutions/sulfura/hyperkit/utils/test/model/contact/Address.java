package solutions.sulfura.hyperkit.utils.test.model.contact;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

@Embeddable
@Dto
public class Address {
    public String addressLine1;
    public String addressLine2;
    public String city;
    public String state;
    public String zipCode;
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    public Country country;

    public Address() {}
}
