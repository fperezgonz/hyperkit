package solutions.sulfura.hyperkit.utils.test.model.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.test.model.dtos.ContactDto;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.utils.test.model.contact.ContactPhone;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(ContactPhone.class)
public class ContactPhoneDto implements Dto<ContactPhone> {

    public ValueWrapper<String> id = ValueWrapper.empty();
    public ValueWrapper<String> number = ValueWrapper.empty();
    public ValueWrapper<ContactDto> contact = ValueWrapper.empty();

    public ContactPhoneDto() {
    }

    public Class<ContactPhone> getSourceClass() {
        return ContactPhone.class;
    }

    public static class Builder {

        ValueWrapper<String> id = ValueWrapper.empty();
        ValueWrapper<String> number = ValueWrapper.empty();
        ValueWrapper<ContactDto> contact = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(final ValueWrapper<String> id){
            this.id = id == null ? ValueWrapper.empty() : id;
            return this;
        }

        public Builder number(final ValueWrapper<String> number){
            this.number = number == null ? ValueWrapper.empty() : number;
            return this;
        }

        public Builder contact(final ValueWrapper<ContactDto> contact){
            this.contact = contact == null ? ValueWrapper.empty() : contact;
            return this;
        }


        public ContactPhoneDto build() {

            ContactPhoneDto instance = new ContactPhoneDto();
            instance.id = id;
            instance.number = number;
            instance.contact = contact;

            return instance;

        }

    }

    @ProjectionFor(ContactPhoneDto.class)
    public static class Projection extends DtoProjection<ContactPhoneDto> {

        public FieldConf id;
        public FieldConf number;
        public DtoFieldConf<ContactDto.Projection> contact;

        public Projection() {
        }

        public void applyProjectionTo(ContactPhoneDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.number = ProjectionUtils.getProjectedValue(dto.number, this.number);
            dto.contact = ProjectionUtils.getProjectedValue(dto.contact, this.contact);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(number, that.number)
                       && Objects.equals(contact, that.contact);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    number,
                    contact);
        }

        public static class Builder {

            FieldConf id;
            FieldConf number;
            DtoFieldConf<ContactDto.Projection> contact;

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

            public Builder number(final FieldConf number){
                this.number = number;
                return this;
            }

            public Builder number(final Presence presence){
                number = FieldConf.of(presence);
                return this;
            }

            public Builder contact(final DtoFieldConf<ContactDto.Projection> contact){
                this.contact = contact;
                return this;
            }

            public Builder contact(final Presence presence, final ContactDto.Projection projection){
                contact = DtoFieldConf.of(presence, projection);
                return this;
            }

            public ContactPhoneDto.Projection build() {

                ContactPhoneDto.Projection instance = new ContactPhoneDto.Projection();
                instance.id = id;
                instance.number = number;
                instance.contact = contact;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _number = "number";
        public static final String _contact = "contact";

    }

}