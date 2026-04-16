package solutions.sulfura.hyperkit.utils.test.model.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.test.model.dtos.AddressDto;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.utils.test.model.scm.Supplier;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(Supplier.class)
public class SupplierDto implements Dto<Supplier> {

    public ValueWrapper<String> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();
    public ValueWrapper<String> email = ValueWrapper.empty();
    public ValueWrapper<String> phone = ValueWrapper.empty();
    public ValueWrapper<AddressDto> address = ValueWrapper.empty();

    public SupplierDto() {
    }

    public Class<Supplier> getSourceClass() {
        return Supplier.class;
    }

    public static class Builder {

        ValueWrapper<String> id = ValueWrapper.empty();
        ValueWrapper<String> name = ValueWrapper.empty();
        ValueWrapper<String> email = ValueWrapper.empty();
        ValueWrapper<String> phone = ValueWrapper.empty();
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

        public Builder email(final ValueWrapper<String> email){
            this.email = email == null ? ValueWrapper.empty() : email;
            return this;
        }

        public Builder phone(final ValueWrapper<String> phone){
            this.phone = phone == null ? ValueWrapper.empty() : phone;
            return this;
        }

        public Builder address(final ValueWrapper<AddressDto> address){
            this.address = address == null ? ValueWrapper.empty() : address;
            return this;
        }


        public SupplierDto build() {

            SupplierDto instance = new SupplierDto();
            instance.id = id;
            instance.name = name;
            instance.email = email;
            instance.phone = phone;
            instance.address = address;

            return instance;

        }

    }

    @ProjectionFor(SupplierDto.class)
    public static class Projection extends DtoProjection<SupplierDto> {

        public FieldConf id;
        public FieldConf name;
        public FieldConf email;
        public FieldConf phone;
        public DtoFieldConf<AddressDto.Projection> address;

        public Projection() {
        }

        public void applyProjectionTo(SupplierDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
            dto.email = ProjectionUtils.getProjectedValue(dto.email, this.email);
            dto.phone = ProjectionUtils.getProjectedValue(dto.phone, this.phone);
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
                       && Objects.equals(email, that.email)
                       && Objects.equals(phone, that.phone)
                       && Objects.equals(address, that.address);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    name,
                    email,
                    phone,
                    address);
        }

        public static class Builder {

            FieldConf id;
            FieldConf name;
            FieldConf email;
            FieldConf phone;
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

            public Builder email(final FieldConf email){
                this.email = email;
                return this;
            }

            public Builder email(final Presence presence){
                email = FieldConf.of(presence);
                return this;
            }

            public Builder phone(final FieldConf phone){
                this.phone = phone;
                return this;
            }

            public Builder phone(final Presence presence){
                phone = FieldConf.of(presence);
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

            public SupplierDto.Projection build() {

                SupplierDto.Projection instance = new SupplierDto.Projection();
                instance.id = id;
                instance.name = name;
                instance.email = email;
                instance.phone = phone;
                instance.address = address;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _name = "name";
        public static final String _email = "email";
        public static final String _phone = "phone";
        public static final String _address = "address";

    }

}