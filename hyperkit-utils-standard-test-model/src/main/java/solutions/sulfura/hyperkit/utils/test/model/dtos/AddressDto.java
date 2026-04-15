package solutions.sulfura.hyperkit.utils.test.model.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.test.model.contact.Address;
import solutions.sulfura.hyperkit.utils.test.model.dtos.CountryDto;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(Address.class)
public class AddressDto implements Dto<Address> {

    public ValueWrapper<String> addressLine1 = ValueWrapper.empty();
    public ValueWrapper<String> addressLine2 = ValueWrapper.empty();
    public ValueWrapper<String> city = ValueWrapper.empty();
    public ValueWrapper<String> state = ValueWrapper.empty();
    public ValueWrapper<String> zipCode = ValueWrapper.empty();
    public ValueWrapper<CountryDto> country = ValueWrapper.empty();

    public AddressDto() {
    }

    public Class<Address> getSourceClass() {
        return Address.class;
    }

    public static class Builder {

        ValueWrapper<String> addressLine1 = ValueWrapper.empty();
        ValueWrapper<String> addressLine2 = ValueWrapper.empty();
        ValueWrapper<String> city = ValueWrapper.empty();
        ValueWrapper<String> state = ValueWrapper.empty();
        ValueWrapper<String> zipCode = ValueWrapper.empty();
        ValueWrapper<CountryDto> country = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder addressLine1(final ValueWrapper<String> addressLine1){
            this.addressLine1 = addressLine1 == null ? ValueWrapper.empty() : addressLine1;
            return this;
        }

        public Builder addressLine2(final ValueWrapper<String> addressLine2){
            this.addressLine2 = addressLine2 == null ? ValueWrapper.empty() : addressLine2;
            return this;
        }

        public Builder city(final ValueWrapper<String> city){
            this.city = city == null ? ValueWrapper.empty() : city;
            return this;
        }

        public Builder state(final ValueWrapper<String> state){
            this.state = state == null ? ValueWrapper.empty() : state;
            return this;
        }

        public Builder zipCode(final ValueWrapper<String> zipCode){
            this.zipCode = zipCode == null ? ValueWrapper.empty() : zipCode;
            return this;
        }

        public Builder country(final ValueWrapper<CountryDto> country){
            this.country = country == null ? ValueWrapper.empty() : country;
            return this;
        }


        public AddressDto build() {

            AddressDto instance = new AddressDto();
            instance.addressLine1 = addressLine1;
            instance.addressLine2 = addressLine2;
            instance.city = city;
            instance.state = state;
            instance.zipCode = zipCode;
            instance.country = country;

            return instance;

        }

    }

    @ProjectionFor(AddressDto.class)
    public static class Projection extends DtoProjection<AddressDto> {

        public FieldConf addressLine1;
        public FieldConf addressLine2;
        public FieldConf city;
        public FieldConf state;
        public FieldConf zipCode;
        public DtoFieldConf<CountryDto.Projection> country;

        public Projection() {
        }

        public void applyProjectionTo(AddressDto dto) throws DtoProjectionException {
            dto.addressLine1 = ProjectionUtils.getProjectedValue(dto.addressLine1, this.addressLine1);
            dto.addressLine2 = ProjectionUtils.getProjectedValue(dto.addressLine2, this.addressLine2);
            dto.city = ProjectionUtils.getProjectedValue(dto.city, this.city);
            dto.state = ProjectionUtils.getProjectedValue(dto.state, this.state);
            dto.zipCode = ProjectionUtils.getProjectedValue(dto.zipCode, this.zipCode);
            dto.country = ProjectionUtils.getProjectedValue(dto.country, this.country);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(addressLine1, that.addressLine1)
                       && Objects.equals(addressLine2, that.addressLine2)
                       && Objects.equals(city, that.city)
                       && Objects.equals(state, that.state)
                       && Objects.equals(zipCode, that.zipCode)
                       && Objects.equals(country, that.country);

        }

        @Override
        public int hashCode() {
            return Objects.hash(addressLine1,
                    addressLine2,
                    city,
                    state,
                    zipCode,
                    country);
        }

        public static class Builder {

            FieldConf addressLine1;
            FieldConf addressLine2;
            FieldConf city;
            FieldConf state;
            FieldConf zipCode;
            DtoFieldConf<CountryDto.Projection> country;

            public static Builder newInstance() {
                return new Builder();
            }

            public Builder addressLine1(final FieldConf addressLine1){
                this.addressLine1 = addressLine1;
                return this;
            }

            public Builder addressLine1(final Presence presence){
                addressLine1 = FieldConf.of(presence);
                return this;
            }

            public Builder addressLine2(final FieldConf addressLine2){
                this.addressLine2 = addressLine2;
                return this;
            }

            public Builder addressLine2(final Presence presence){
                addressLine2 = FieldConf.of(presence);
                return this;
            }

            public Builder city(final FieldConf city){
                this.city = city;
                return this;
            }

            public Builder city(final Presence presence){
                city = FieldConf.of(presence);
                return this;
            }

            public Builder state(final FieldConf state){
                this.state = state;
                return this;
            }

            public Builder state(final Presence presence){
                state = FieldConf.of(presence);
                return this;
            }

            public Builder zipCode(final FieldConf zipCode){
                this.zipCode = zipCode;
                return this;
            }

            public Builder zipCode(final Presence presence){
                zipCode = FieldConf.of(presence);
                return this;
            }

            public Builder country(final DtoFieldConf<CountryDto.Projection> country){
                this.country = country;
                return this;
            }

            public Builder country(final Presence presence, final CountryDto.Projection projection){
                country = DtoFieldConf.of(presence, projection);
                return this;
            }

            public AddressDto.Projection build() {

                AddressDto.Projection instance = new AddressDto.Projection();
                instance.addressLine1 = addressLine1;
                instance.addressLine2 = addressLine2;
                instance.city = city;
                instance.state = state;
                instance.zipCode = zipCode;
                instance.country = country;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _addressLine1 = "addressLine1";
        public static final String _addressLine2 = "addressLine2";
        public static final String _city = "city";
        public static final String _state = "state";
        public static final String _zipCode = "zipCode";
        public static final String _country = "country";

    }

}