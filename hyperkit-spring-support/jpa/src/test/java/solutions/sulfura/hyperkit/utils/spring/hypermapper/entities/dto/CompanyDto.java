package solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.dto;

import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.Company;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(Company.class)
public class CompanyDto implements Dto<Company> {

    public ValueWrapper<Long> id = ValueWrapper.empty();

    public CompanyDto() {
    }

    public Class<Company> getSourceClass() {
        return Company.class;
    }

    public static class Builder {

        ValueWrapper<Long> id = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(final ValueWrapper<Long> id){
            this.id = id == null ? ValueWrapper.empty() : id;
            return this;
        }


        public CompanyDto build() {

            CompanyDto instance = new CompanyDto();
            instance.id = id;

            return instance;

        }

    }

    @ProjectionFor(CompanyDto.class)
    public static class Projection extends DtoProjection<CompanyDto> {

        public FieldConf id;

        public Projection() {
        }

        public void applyProjectionTo(CompanyDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        public static class Builder {

            FieldConf id;

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

            public CompanyDto.Projection build() {

                CompanyDto.Projection instance = new CompanyDto.Projection();
                instance.id = id;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";

    }

}