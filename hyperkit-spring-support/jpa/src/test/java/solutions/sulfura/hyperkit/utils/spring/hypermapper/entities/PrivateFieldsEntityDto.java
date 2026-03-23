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

@DtoFor(PrivateFieldsEntity.class)
public class PrivateFieldsEntityDto implements Dto<PrivateFieldsEntity> {

    public ValueWrapper<Long> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();

    public PrivateFieldsEntityDto() {}

    @Override
    public Class<PrivateFieldsEntity> getSourceClass() {
        return PrivateFieldsEntity.class;
    }

    public static class Builder {
        ValueWrapper<Long> id = ValueWrapper.empty();
        ValueWrapper<String> name = ValueWrapper.empty();

        public static Builder newInstance() { return new Builder(); }
        public Builder id(ValueWrapper<Long> id) { this.id = id == null ? ValueWrapper.empty() : id; return this; }
        public Builder name(ValueWrapper<String> name) { this.name = name == null ? ValueWrapper.empty() : name; return this; }
        public PrivateFieldsEntityDto build() { var dto = new PrivateFieldsEntityDto(); dto.id = id; dto.name = name; return dto; }
    }

    @ProjectionFor(PrivateFieldsEntityDto.class)
    public static class Projection extends DtoProjection<PrivateFieldsEntityDto> {
        public FieldConf id;
        public FieldConf name;
        public Projection() {}
        @Override
        public void applyProjectionTo(PrivateFieldsEntityDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
        }
        public static class Builder {
            FieldConf id;
            FieldConf name;
            public static Builder newInstance() { return new Builder(); }
            public Builder id(FieldConf id) { this.id = id; return this; }
            public Builder id(Presence presence) { this.id = FieldConf.of(presence); return this; }
            public Builder name(FieldConf name) { this.name = name; return this; }
            public Builder name(Presence presence) { this.name = FieldConf.of(presence); return this; }
            public Projection build() { var p = new Projection(); p.id = id; p.name = name; return p; }
        }
    }

    public static class DtoModel {
        public static final String _id = "id";
        public static final String _name = "name";
    }
}
