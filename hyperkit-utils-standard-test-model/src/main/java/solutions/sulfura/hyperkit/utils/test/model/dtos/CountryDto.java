package solutions.sulfura.hyperkit.utils.test.model.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.utils.test.model.contact.Country;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(Country.class)
public class CountryDto implements Dto<Country> {

    public ValueWrapper<String> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();
    public ValueWrapper<String> isoAlpha2 = ValueWrapper.empty();
    public ValueWrapper<String> isoAlpha3 = ValueWrapper.empty();

    public CountryDto() {
    }

    public Class<Country> getSourceClass() {
        return Country.class;
    }

    public static class Builder {

        ValueWrapper<String> id = ValueWrapper.empty();
        ValueWrapper<String> name = ValueWrapper.empty();
        ValueWrapper<String> isoAlpha2 = ValueWrapper.empty();
        ValueWrapper<String> isoAlpha3 = ValueWrapper.empty();

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

        public Builder isoAlpha2(final ValueWrapper<String> isoAlpha2){
            this.isoAlpha2 = isoAlpha2 == null ? ValueWrapper.empty() : isoAlpha2;
            return this;
        }

        public Builder isoAlpha3(final ValueWrapper<String> isoAlpha3){
            this.isoAlpha3 = isoAlpha3 == null ? ValueWrapper.empty() : isoAlpha3;
            return this;
        }


        public CountryDto build() {

            CountryDto instance = new CountryDto();
            instance.id = id;
            instance.name = name;
            instance.isoAlpha2 = isoAlpha2;
            instance.isoAlpha3 = isoAlpha3;

            return instance;

        }

    }

    @ProjectionFor(CountryDto.class)
    public static class Projection extends DtoProjection<CountryDto> {

        public FieldConf id;
        public FieldConf name;
        public FieldConf isoAlpha2;
        public FieldConf isoAlpha3;

        public Projection() {
        }

        public void applyProjectionTo(CountryDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
            dto.isoAlpha2 = ProjectionUtils.getProjectedValue(dto.isoAlpha2, this.isoAlpha2);
            dto.isoAlpha3 = ProjectionUtils.getProjectedValue(dto.isoAlpha3, this.isoAlpha3);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(name, that.name)
                       && Objects.equals(isoAlpha2, that.isoAlpha2)
                       && Objects.equals(isoAlpha3, that.isoAlpha3);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    name,
                    isoAlpha2,
                    isoAlpha3);
        }

        public static class Builder {

            FieldConf id;
            FieldConf name;
            FieldConf isoAlpha2;
            FieldConf isoAlpha3;

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

            public Builder isoAlpha2(final FieldConf isoAlpha2){
                this.isoAlpha2 = isoAlpha2;
                return this;
            }

            public Builder isoAlpha2(final Presence presence){
                isoAlpha2 = FieldConf.of(presence);
                return this;
            }

            public Builder isoAlpha3(final FieldConf isoAlpha3){
                this.isoAlpha3 = isoAlpha3;
                return this;
            }

            public Builder isoAlpha3(final Presence presence){
                isoAlpha3 = FieldConf.of(presence);
                return this;
            }

            public CountryDto.Projection build() {

                CountryDto.Projection instance = new CountryDto.Projection();
                instance.id = id;
                instance.name = name;
                instance.isoAlpha2 = isoAlpha2;
                instance.isoAlpha3 = isoAlpha3;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _name = "name";
        public static final String _isoAlpha2 = "isoAlpha2";
        public static final String _isoAlpha3 = "isoAlpha3";

    }

}