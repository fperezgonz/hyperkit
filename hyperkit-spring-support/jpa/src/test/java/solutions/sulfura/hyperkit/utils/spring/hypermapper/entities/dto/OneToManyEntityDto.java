package solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.dto;

import solutions.sulfura.hyperkit.dtos.Dto;
import java.util.Set;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.dto.CompanyDto;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.dto.ManyToOneEntityDto;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.OneToManyEntity;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(OneToManyEntity.class)
public class OneToManyEntityDto implements Dto<OneToManyEntity> {

    public ValueWrapper<Long> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();
    public ValueWrapper<String> description = ValueWrapper.empty();
    public ValueWrapper<Set<ListOperation<ManyToOneEntityDto>>> manyToOneEntities = ValueWrapper.empty();
    public ValueWrapper<CompanyDto> company = ValueWrapper.empty();

    public OneToManyEntityDto() {
    }

    public Class<OneToManyEntity> getSourceClass() {
        return OneToManyEntity.class;
    }

    public static class Builder {

        ValueWrapper<Long> id = ValueWrapper.empty();
        ValueWrapper<String> name = ValueWrapper.empty();
        ValueWrapper<String> description = ValueWrapper.empty();
        ValueWrapper<Set<ListOperation<ManyToOneEntityDto>>> manyToOneEntities = ValueWrapper.empty();
        ValueWrapper<CompanyDto> company = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(final ValueWrapper<Long> id){
            this.id = id == null ? ValueWrapper.empty() : id;
            return this;
        }

        public Builder name(final ValueWrapper<String> name){
            this.name = name == null ? ValueWrapper.empty() : name;
            return this;
        }

        public Builder description(final ValueWrapper<String> description){
            this.description = description == null ? ValueWrapper.empty() : description;
            return this;
        }

        public Builder manyToOneEntities(final ValueWrapper<Set<ListOperation<ManyToOneEntityDto>>> manyToOneEntities){
            this.manyToOneEntities = manyToOneEntities == null ? ValueWrapper.empty() : manyToOneEntities;
            return this;
        }

        public Builder company(final ValueWrapper<CompanyDto> company){
            this.company = company == null ? ValueWrapper.empty() : company;
            return this;
        }


        public OneToManyEntityDto build() {

            OneToManyEntityDto instance = new OneToManyEntityDto();
            instance.id = id;
            instance.name = name;
            instance.description = description;
            instance.manyToOneEntities = manyToOneEntities;
            instance.company = company;

            return instance;

        }

    }

    @ProjectionFor(OneToManyEntityDto.class)
    public static class Projection extends DtoProjection<OneToManyEntityDto> {

        public FieldConf id;
        public FieldConf name;
        public FieldConf description;
        public DtoListFieldConf<ManyToOneEntityDto.Projection> manyToOneEntities;
        public DtoFieldConf<CompanyDto.Projection> company;

        public Projection() {
        }

        public void applyProjectionTo(OneToManyEntityDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
            dto.description = ProjectionUtils.getProjectedValue(dto.description, this.description);
            dto.manyToOneEntities = ProjectionUtils.getProjectedValue(dto.manyToOneEntities, this.manyToOneEntities);
            dto.company = ProjectionUtils.getProjectedValue(dto.company, this.company);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(name, that.name)
                       && Objects.equals(description, that.description)
                       && Objects.equals(manyToOneEntities, that.manyToOneEntities)
                       && Objects.equals(company, that.company);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    name,
                    description,
                    manyToOneEntities,
                    company);
        }

        public static class Builder {

            FieldConf id;
            FieldConf name;
            FieldConf description;
            DtoListFieldConf<ManyToOneEntityDto.Projection> manyToOneEntities;
            DtoFieldConf<CompanyDto.Projection> company;

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

            public Builder description(final FieldConf description){
                this.description = description;
                return this;
            }

            public Builder description(final Presence presence){
                description = FieldConf.of(presence);
                return this;
            }

            public Builder manyToOneEntities(final DtoListFieldConf<ManyToOneEntityDto.Projection> manyToOneEntities){
                this.manyToOneEntities = manyToOneEntities;
                return this;
            }

            public Builder manyToOneEntities(final Presence presence, final ManyToOneEntityDto.Projection projection){
                manyToOneEntities = DtoListFieldConf.of(presence, projection);
                return this;
            }

            public Builder company(final DtoFieldConf<CompanyDto.Projection> company){
                this.company = company;
                return this;
            }

            public Builder company(final Presence presence, final CompanyDto.Projection projection){
                company = DtoFieldConf.of(presence, projection);
                return this;
            }

            public OneToManyEntityDto.Projection build() {

                OneToManyEntityDto.Projection instance = new OneToManyEntityDto.Projection();
                instance.id = id;
                instance.name = name;
                instance.description = description;
                instance.manyToOneEntities = manyToOneEntities;
                instance.company = company;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _name = "name";
        public static final String _description = "description";
        public static final String _manyToOneEntities = "manyToOneEntities";
        public static final String _company = "company";

    }

}