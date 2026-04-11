package solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.dto;

import solutions.sulfura.hyperkit.dtos.Dto;
import java.util.Set;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.dto.ManyToManyRightEntityDto;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.ManyToManyLeftEntity;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(ManyToManyLeftEntity.class)
public class ManyToManyLeftEntityDto implements Dto<ManyToManyLeftEntity> {

    public ValueWrapper<Long> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();
    public ValueWrapper<Set<ListOperation<ManyToManyRightEntityDto>>> rights = ValueWrapper.empty();

    public ManyToManyLeftEntityDto() {
    }

    public Class<ManyToManyLeftEntity> getSourceClass() {
        return ManyToManyLeftEntity.class;
    }

    public static class Builder {

        ValueWrapper<Long> id = ValueWrapper.empty();
        ValueWrapper<String> name = ValueWrapper.empty();
        ValueWrapper<Set<ListOperation<ManyToManyRightEntityDto>>> rights = ValueWrapper.empty();

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

        public Builder rights(final ValueWrapper<Set<ListOperation<ManyToManyRightEntityDto>>> rights){
            this.rights = rights == null ? ValueWrapper.empty() : rights;
            return this;
        }


        public ManyToManyLeftEntityDto build() {

            ManyToManyLeftEntityDto instance = new ManyToManyLeftEntityDto();
            instance.id = id;
            instance.name = name;
            instance.rights = rights;

            return instance;

        }

    }

    @ProjectionFor(ManyToManyLeftEntityDto.class)
    public static class Projection extends DtoProjection<ManyToManyLeftEntityDto> {

        public FieldConf id;
        public FieldConf name;
        public DtoListFieldConf<ManyToManyRightEntityDto.Projection> rights;

        public Projection() {
        }

        public void applyProjectionTo(ManyToManyLeftEntityDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
            dto.rights = ProjectionUtils.getProjectedValue(dto.rights, this.rights);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(name, that.name)
                       && Objects.equals(rights, that.rights);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    name,
                    rights);
        }

        public static class Builder {

            FieldConf id;
            FieldConf name;
            DtoListFieldConf<ManyToManyRightEntityDto.Projection> rights;

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

            public Builder rights(final DtoListFieldConf<ManyToManyRightEntityDto.Projection> rights){
                this.rights = rights;
                return this;
            }

            public Builder rights(final Presence presence, final ManyToManyRightEntityDto.Projection projection){
                rights = DtoListFieldConf.of(presence, projection);
                return this;
            }

            public ManyToManyLeftEntityDto.Projection build() {

                ManyToManyLeftEntityDto.Projection instance = new ManyToManyLeftEntityDto.Projection();
                instance.id = id;
                instance.name = name;
                instance.rights = rights;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _name = "name";
        public static final String _rights = "rights";

    }

}