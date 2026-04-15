package solutions.sulfura.hyperkit.utils.test.model.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.test.model.dtos.ContactDto;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.utils.test.model.contact.ContactEmail;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(ContactEmail.class)
public class ContactEmailDto implements Dto<ContactEmail> {

    public ValueWrapper<String> id = ValueWrapper.empty();
    public ValueWrapper<String> email = ValueWrapper.empty();
    public ValueWrapper<ContactDto> contact = ValueWrapper.empty();

    public ContactEmailDto() {
    }

    public Class<ContactEmail> getSourceClass() {
        return ContactEmail.class;
    }

    public static class Builder {

        ValueWrapper<String> id = ValueWrapper.empty();
        ValueWrapper<String> email = ValueWrapper.empty();
        ValueWrapper<ContactDto> contact = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(final ValueWrapper<String> id){
            this.id = id == null ? ValueWrapper.empty() : id;
            return this;
        }

        public Builder email(final ValueWrapper<String> email){
            this.email = email == null ? ValueWrapper.empty() : email;
            return this;
        }

        public Builder contact(final ValueWrapper<ContactDto> contact){
            this.contact = contact == null ? ValueWrapper.empty() : contact;
            return this;
        }


        public ContactEmailDto build() {

            ContactEmailDto instance = new ContactEmailDto();
            instance.id = id;
            instance.email = email;
            instance.contact = contact;

            return instance;

        }

    }

    @ProjectionFor(ContactEmailDto.class)
    public static class Projection extends DtoProjection<ContactEmailDto> {

        public FieldConf id;
        public FieldConf email;
        public DtoFieldConf<ContactDto.Projection> contact;

        public Projection() {
        }

        public void applyProjectionTo(ContactEmailDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.email = ProjectionUtils.getProjectedValue(dto.email, this.email);
            dto.contact = ProjectionUtils.getProjectedValue(dto.contact, this.contact);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(email, that.email)
                       && Objects.equals(contact, that.contact);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    email,
                    contact);
        }

        public static class Builder {

            FieldConf id;
            FieldConf email;
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

            public Builder email(final FieldConf email){
                this.email = email;
                return this;
            }

            public Builder email(final Presence presence){
                email = FieldConf.of(presence);
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

            public ContactEmailDto.Projection build() {

                ContactEmailDto.Projection instance = new ContactEmailDto.Projection();
                instance.id = id;
                instance.email = email;
                instance.contact = contact;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _email = "email";
        public static final String _contact = "contact";

    }

}