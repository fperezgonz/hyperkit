package solutions.sulfura.hyperkit.utils.test.model.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.test.model.scm.shipments.Shipment;
import solutions.sulfura.hyperkit.utils.test.model.dtos.SupplierDto;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.utils.test.model.scm.shipments.Shipment.SupplierRef;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(Shipment.SupplierRef.class)
public class SupplierRefDto implements Dto<Shipment.SupplierRef> {

    public ValueWrapper<String> name = ValueWrapper.empty();
    public ValueWrapper<String> email = ValueWrapper.empty();
    public ValueWrapper<String> phone = ValueWrapper.empty();
    public ValueWrapper<String> address = ValueWrapper.empty();
    public ValueWrapper<SupplierDto> supplier = ValueWrapper.empty();

    public SupplierRefDto() {
    }

    public Class<Shipment.SupplierRef> getSourceClass() {
        return Shipment.SupplierRef.class;
    }

    public static class Builder {

        ValueWrapper<String> name = ValueWrapper.empty();
        ValueWrapper<String> email = ValueWrapper.empty();
        ValueWrapper<String> phone = ValueWrapper.empty();
        ValueWrapper<String> address = ValueWrapper.empty();
        ValueWrapper<SupplierDto> supplier = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
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

        public Builder address(final ValueWrapper<String> address){
            this.address = address == null ? ValueWrapper.empty() : address;
            return this;
        }

        public Builder supplier(final ValueWrapper<SupplierDto> supplier){
            this.supplier = supplier == null ? ValueWrapper.empty() : supplier;
            return this;
        }


        public SupplierRefDto build() {

            SupplierRefDto instance = new SupplierRefDto();
            instance.name = name;
            instance.email = email;
            instance.phone = phone;
            instance.address = address;
            instance.supplier = supplier;

            return instance;

        }

    }

    @ProjectionFor(SupplierRefDto.class)
    public static class Projection extends DtoProjection<SupplierRefDto> {

        public FieldConf name;
        public FieldConf email;
        public FieldConf phone;
        public FieldConf address;
        public DtoFieldConf<SupplierDto.Projection> supplier;

        public Projection() {
        }

        public void applyProjectionTo(SupplierRefDto dto) throws DtoProjectionException {
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
            dto.email = ProjectionUtils.getProjectedValue(dto.email, this.email);
            dto.phone = ProjectionUtils.getProjectedValue(dto.phone, this.phone);
            dto.address = ProjectionUtils.getProjectedValue(dto.address, this.address);
            dto.supplier = ProjectionUtils.getProjectedValue(dto.supplier, this.supplier);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(name, that.name)
                       && Objects.equals(email, that.email)
                       && Objects.equals(phone, that.phone)
                       && Objects.equals(address, that.address)
                       && Objects.equals(supplier, that.supplier);

        }

        @Override
        public int hashCode() {
            return Objects.hash(name,
                    email,
                    phone,
                    address,
                    supplier);
        }

        public static class Builder {

            FieldConf name;
            FieldConf email;
            FieldConf phone;
            FieldConf address;
            DtoFieldConf<SupplierDto.Projection> supplier;

            public static Builder newInstance() {
                return new Builder();
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

            public Builder address(final FieldConf address){
                this.address = address;
                return this;
            }

            public Builder address(final Presence presence){
                address = FieldConf.of(presence);
                return this;
            }

            public Builder supplier(final DtoFieldConf<SupplierDto.Projection> supplier){
                this.supplier = supplier;
                return this;
            }

            public Builder supplier(final Presence presence, final SupplierDto.Projection projection){
                supplier = DtoFieldConf.of(presence, projection);
                return this;
            }

            public SupplierRefDto.Projection build() {

                SupplierRefDto.Projection instance = new SupplierRefDto.Projection();
                instance.name = name;
                instance.email = email;
                instance.phone = phone;
                instance.address = address;
                instance.supplier = supplier;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _name = "name";
        public static final String _email = "email";
        public static final String _phone = "phone";
        public static final String _address = "address";
        public static final String _supplier = "supplier";

    }

}