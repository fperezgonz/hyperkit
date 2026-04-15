package solutions.sulfura.hyperkit.utils.test.model.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import java.util.Set;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.utils.test.model.dtos.AddressDto;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.hyperkit.utils.test.model.contact.Contact;
import solutions.sulfura.hyperkit.utils.test.model.dtos.ContactEmailDto;
import solutions.sulfura.hyperkit.utils.test.model.dtos.ContactPhoneDto;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(Contact.class)
public class ContactDto implements Dto<Contact> {

    public ValueWrapper<String> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();
    public ValueWrapper<Set<ListOperation<ContactEmailDto>>> emails = ValueWrapper.empty();
    public ValueWrapper<Set<ListOperation<ContactPhoneDto>>> phones = ValueWrapper.empty();
    public ValueWrapper<AddressDto> address = ValueWrapper.empty();

    public ContactDto() {
    }

    public Class<Contact> getSourceClass() {
        return Contact.class;
    }

    public static class Builder {

        ValueWrapper<String> id = ValueWrapper.empty();
        ValueWrapper<String> name = ValueWrapper.empty();
        ValueWrapper<Set<ListOperation<ContactEmailDto>>> emails = ValueWrapper.empty();
        ValueWrapper<Set<ListOperation<ContactPhoneDto>>> phones = ValueWrapper.empty();
        ValueWrapper<AddressDto> address = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(final ValueWrapper<String> id){
            this.id = id == null ? ValueWrapper.empty() : id;
            return this;
        }

        public Builder name(final ValueWrapper<String> name){
            this.name = name == null ? ValueWrapper.empty() : name;
            return this;
        }

        public Builder emails(final ValueWrapper<Set<ListOperation<ContactEmailDto>>> emails){
            this.emails = emails == null ? ValueWrapper.empty() : emails;
            return this;
        }

        public Builder phones(final ValueWrapper<Set<ListOperation<ContactPhoneDto>>> phones){
            this.phones = phones == null ? ValueWrapper.empty() : phones;
            return this;
        }

        public Builder address(final ValueWrapper<AddressDto> address){
            this.address = address == null ? ValueWrapper.empty() : address;
            return this;
        }


        public ContactDto build() {

            ContactDto instance = new ContactDto();
            instance.id = id;
            instance.name = name;
            instance.emails = emails;
            instance.phones = phones;
            instance.address = address;

            return instance;

        }

    }

    @ProjectionFor(ContactDto.class)
    public static class Projection extends DtoProjection<ContactDto> {

        public FieldConf id;
        public FieldConf name;
        public DtoListFieldConf<ContactPhoneDto.Projection> emails;
        public DtoListFieldConf<ContactPhoneDto.Projection> phones;
        public DtoFieldConf<AddressDto.Projection> address;

        public Projection() {
        }

        public void applyProjectionTo(ContactDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
            dto.emails = ProjectionUtils.getProjectedValue(dto.emails, this.emails);
            dto.phones = ProjectionUtils.getProjectedValue(dto.phones, this.phones);
            dto.address = ProjectionUtils.getProjectedValue(dto.address, this.address);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(name, that.name)
                       && Objects.equals(emails, that.emails)
                       && Objects.equals(phones, that.phones)
                       && Objects.equals(address, that.address);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    name,
                    emails,
                    phones,
                    address);
        }

        public static class Builder {

            FieldConf id;
            FieldConf name;
            DtoListFieldConf<ContactPhoneDto.Projection> emails;
            DtoListFieldConf<ContactPhoneDto.Projection> phones;
            DtoFieldConf<AddressDto.Projection> address;

            public static Builder newInstance() {
                return new Builder();
            }

            public Builder id(final FieldConf id){
                this.id = id;
                return this;
            }

            public Builder id(final Presence presence){
                id = FieldConf.of(presence);
                return this;
            }

            public Builder name(final FieldConf name){
                this.name = name;
                return this;
            }

            public Builder name(final Presence presence){
                name = FieldConf.of(presence);
                return this;
            }

            public Builder emails(final DtoListFieldConf<ContactPhoneDto.Projection> emails){
                this.emails = emails;
                return this;
            }

            public Builder emails(final Presence presence, final ContactPhoneDto.Projection projection){
                emails = DtoListFieldConf.of(presence, projection);
                return this;
            }

            public Builder phones(final DtoListFieldConf<ContactPhoneDto.Projection> phones){
                this.phones = phones;
                return this;
            }

            public Builder phones(final Presence presence, final ContactPhoneDto.Projection projection){
                phones = DtoListFieldConf.of(presence, projection);
                return this;
            }

            public Builder address(final DtoFieldConf<AddressDto.Projection> address){
                this.address = address;
                return this;
            }

            public Builder address(final Presence presence, final AddressDto.Projection projection){
                address = DtoFieldConf.of(presence, projection);
                return this;
            }

            public ContactDto.Projection build() {

                ContactDto.Projection instance = new ContactDto.Projection();
                instance.id = id;
                instance.name = name;
                instance.emails = emails;
                instance.phones = phones;
                instance.address = address;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _name = "name";
        public static final String _emails = "emails";
        public static final String _phones = "phones";
        public static final String _address = "address";

    }

}