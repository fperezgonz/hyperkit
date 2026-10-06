package solutions.sulfura.hyperkit.utils.spring.hypermapper.entities;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;

import java.util.Set;

@SuppressWarnings("unused")
@DtoFor(OwningOneToManyEntity.class)
public class OwningOneToManyEntityDto implements Dto<OwningOneToManyEntity> {

    public ValueWrapper<Long> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();
    public ValueWrapper<Set<ListOperation<OwningOneToManyChildEntityDto>>> children = ValueWrapper.empty();

    public OwningOneToManyEntityDto() {
    }

    public Class<OwningOneToManyEntity> getSourceClass() {
        return OwningOneToManyEntity.class;
    }

    public static class Builder {

        ValueWrapper<Long> id = ValueWrapper.empty();
        ValueWrapper<String> name = ValueWrapper.empty();
        ValueWrapper<Set<ListOperation<OwningOneToManyChildEntityDto>>> children = ValueWrapper.empty();

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

        public Builder children(final ValueWrapper<Set<ListOperation<OwningOneToManyChildEntityDto>>> children) {
            this.children = children == null ? ValueWrapper.empty() : children;
            return this;
        }

        public OwningOneToManyEntityDto build() {
            OwningOneToManyEntityDto instance = new OwningOneToManyEntityDto();
            instance.id = id;
            instance.name = name;
            instance.children = children;
            return instance;
        }

    }

    @ProjectionFor(OwningOneToManyEntityDto.class)
    public static class Projection extends DtoProjection<OwningOneToManyEntityDto> {

        public FieldConf id;
        public FieldConf name;
        public DtoListFieldConf<OwningOneToManyChildEntityDto.Projection> children;

        public Projection() {
        }

        public void applyProjectionTo(OwningOneToManyEntityDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
            dto.children = ProjectionUtils.getProjectedValue(dto.children, this.children);
        }

        public static class Builder {

            FieldConf id;
            FieldConf name;
            DtoListFieldConf<OwningOneToManyChildEntityDto.Projection> children;

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

            public Builder children(final DtoListFieldConf<OwningOneToManyChildEntityDto.Projection> children) {
                this.children = children;
                return this;
            }

            public Builder children(final Presence presence, final OwningOneToManyChildEntityDto.Projection projection) {
                this.children = DtoListFieldConf.of(presence, projection);
                return this;
            }

            public Projection build() {
                Projection instance = new Projection();
                instance.id = id;
                instance.name = name;
                instance.children = children;
                return instance;
            }

        }

    }

    public static class DtoModel {
        public static final String _id = "id";
        public static final String _name = "name";
        public static final String _children = "children";
    }

}
