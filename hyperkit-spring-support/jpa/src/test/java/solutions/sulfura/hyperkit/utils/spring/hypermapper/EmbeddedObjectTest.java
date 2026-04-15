package solutions.sulfura.hyperkit.utils.spring.hypermapper;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.spring.HyperRepositoryImpl;
import solutions.sulfura.hyperkit.utils.test.model.contact.*;
import solutions.sulfura.hyperkit.utils.test.model.dtos.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class EmbeddedObjectTest {

    @Autowired
    private HyperMapper<Object> hyperMapper;
    @Autowired
    private HyperRepositoryImpl<Object> hyperRepository;

    Country createAndPersistTestCountry(String id, String name) {
        Country country = new Country();
        country.id = id;
        country.name = name;
        return hyperRepository.save(country, null);
    }

    Address createTestAddress(String addressLine1, String addressLine2, String city, String state, String zipCode, Country country) {
        Address address = new Address();
        address.addressLine1 = addressLine1;
        address.addressLine2 = addressLine2;
        address.city = city;
        address.state = state;
        address.zipCode = zipCode;
        address.country = country;
        return address;
    }

    Contact createAndPersistTestContact(String id, String name, Address address) {
        Contact contact = new Contact();
        contact.id = id;
        contact.name = name;
        contact.address = address;
        return hyperRepository.save(contact, null);
    }

    ContactEmail createAndPersistTestEmail(String email, Contact contact) {
        ContactEmail result = new ContactEmail();
        result.email = email;
        result.contact = contact;
        hyperRepository.save(result, null);
        return result;
    }

    ContactPhone createAndPersistTestPhone(String number, Contact contact) {
        ContactPhone result = new ContactPhone();
        result.number = number;
        result.contact = contact;
        hyperRepository.save(result, null);
        return result;
    }


    @Test
    @DisplayName("Embedded properties are mapped, persisted and relationships are handled correctly")
    @Transactional
    public void testEmbeddedProperties() {
        // Given a Contact entity with an embedded Address
        Country countrySpain = createAndPersistTestCountry("ESP", "SPAIN");
        Address testAddress = createTestAddress("123 Main St", "Apt 456", "Anytown", "CA", "12345", null);
        testAddress.country = countrySpain;
        Contact contact = createAndPersistTestContact("WH1", "Main contact", testAddress);
        contact.address = testAddress;

        hyperRepository.save(contact, null);

        ContactPhone contactPhone1 = createAndPersistTestPhone("666666666", contact);
        ContactPhone contactPhone2 = createAndPersistTestPhone("999999999", contact);

        HashSet<ContactPhone> contactPhones = new HashSet<>();
        contactPhones.add(contactPhone1);
        contactPhones.add(contactPhone2);
        contact.phones = contactPhones;

        ContactEmail contactEmail1 = createAndPersistTestEmail("lizard@wizard.com", contact);
        ContactEmail contactEmail2 = createAndPersistTestEmail("wizard@lizard.com", contact);

        HashSet<ContactEmail> contactEmails = new HashSet<>();
        contactEmails.add(contactEmail1);
        contactEmails.add(contactEmail2);
        contact.emails = contactEmails;

        Country countryFrance = createAndPersistTestCountry("FRA", "FRANCE");
        hyperRepository.save(countryFrance, null);

        // When mapping the new data through a Dto
        ContactDto contactDto = ContactDto.Builder.newInstance()
                .id(ValueWrapper.of(contact.id))
                .name(ValueWrapper.of("New contact name"))
                .phones(ValueWrapper.of(Set.of(ListOperation.valueOf(ContactPhoneDto.Builder.newInstance()
                        .number(ValueWrapper.of("1"))
                        .build(), ListOperation.ListOperationType.ADD, ListOperation.ItemOperationType.INSERT))))
                .emails(ValueWrapper.of(Set.of(
                        ListOperation.valueOf(ContactEmailDto.Builder.newInstance()
                                .id(ValueWrapper.of(contactEmail1.id))
                                .email(ValueWrapper.of("lizard2@wizard.com"))
                                .build(), ListOperation.ListOperationType.NONE, ListOperation.ItemOperationType.UPDATE),
                        ListOperation.valueOf(ContactEmailDto.Builder.newInstance()
                                .id(ValueWrapper.of(contactEmail2.id))
                                .build(), ListOperation.ListOperationType.REMOVE, ListOperation.ItemOperationType.NONE)
                )))
                .address(ValueWrapper.of(AddressDto.Builder.newInstance()
                        .addressLine1(ValueWrapper.of("New Address"))
                        .country(ValueWrapper.of(CountryDto.Builder.newInstance()
                                .id(ValueWrapper.of(countryFrance.id))
                                .isoAlpha2(ValueWrapper.of("FRA"))
                                .build()))
                        .build()))
                .build();

        Contact persistedContact = hyperMapper.persistDtoToEntity(contactDto, null);

        // Then the Dto data and the relationships between the entities are mapped and persisted correctly
        assertNotNull(persistedContact);
        assertEquals("New contact name", persistedContact.name);
        assertNotNull(persistedContact.address);
        assertEquals("New Address", persistedContact.address.addressLine1);
        assertEquals(countryFrance.id, persistedContact.address.country.id);

        assertEquals(3, persistedContact.phones.size());
        ContactPhone newPhone = persistedContact.phones.stream()
                .filter(p -> Objects.equals(p.number, "1"))
                .findFirst()
                .orElse(null);
        assertNotNull(newPhone);

        assertEquals(1, persistedContact.emails.size());
        assertEquals("lizard2@wizard.com", persistedContact.emails.stream().findFirst().get().email);

        assertNull(hyperRepository.findById(ContactEmail.class, contactEmail2.id, null)
                .orElseThrow()
                .contact);


    }


}
