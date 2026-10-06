package solutions.sulfura.hyperkit.utils.spring.hypermapper.entities;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;

@SuppressWarnings("unused")
@DtoFor(OwningOneToManyChildEntity.class)
public class OwningOneToManyChildEntityDto implements Dto<OwningOneToManyChildEntity> {

    public ValueWrapper<Long> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();

    public OwningOneToManyChildEntityDto() {
    }

    public Class<OwningOneToManyChildEntity> getSourceClass() {
        return OwningOneToManyChildEntity.class;
    }

    public static class Builder {

        ValueWrapper<Long> id = ValueWrapper.empty();
        ValueWrapper<String> name = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(final ValueWrapper<Long> id) {
            this.id = id == null ? ValueWrapper.empty() : id;
            return this;
        }

        public Builder name(final ValueWrapper<String> name) {
            this.name = name == null ? ValueWrapper.empty() : name;
            return this;
        }

        public OwningOneToManyChildEntityDto build() {
            OwningOneToManyChildEntityDto instance = new OwningOneToManyChildEntityDto();
            instance.id = id;
            instance.name = name;
            return instance;
        }

    }

    @ProjectionFor(OwningOneToManyChildEntityDto.class)
    public static class Projection extends DtoProjection<OwningOneToManyChildEntityDto> {

        public FieldConf id;
        public FieldConf name;

        public Projection() {
        }

        public void applyProjectionTo(OwningOneToManyChildEntityDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
        }

        public static class Builder {

            FieldConf id;
            FieldConf name;

            public static Builder newInstance() {
                return new Builder();
            }

            public Builder id(final FieldConf id) {
                this.id = id;
                return this;
            }

            public Builder id(final Presence presence) {
                id = FieldConf.of(presence);
                return this;
            }

            public Builder name(final FieldConf name) {
                this.name = name;
                return this;
            }

            public Builder name(final Presence presence) {
                name = FieldConf.of(presence);
                return this;
            }

            public Projection build() {
                Projection instance = new Projection();
                instance.id = id;
                instance.name = name;
                return instance;
            }

        }

    }

    public static class DtoModel {
        public static final String _id = "id";
        public static final String _name = "name";
    }

}
