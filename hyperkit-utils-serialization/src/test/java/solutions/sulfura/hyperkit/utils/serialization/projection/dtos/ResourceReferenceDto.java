package solutions.sulfura.hyperkit.utils.serialization.projection.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.utils.serialization.projection.model.ResourceReference;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(ResourceReference.class)
public class ResourceReferenceDto implements Dto<ResourceReference> {

    public ValueWrapper<String> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();
    public ValueWrapper<String> type = ValueWrapper.empty();

    public ResourceReferenceDto() {
    }

    public Class<ResourceReference> getSourceClass() {
        return ResourceReference.class;
    }

    public static class Builder {

        ValueWrapper<String> id = ValueWrapper.empty();
        ValueWrapper<String> name = ValueWrapper.empty();
        ValueWrapper<String> type = ValueWrapper.empty();

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

        public Builder type(final ValueWrapper<String> type){
            this.type = type == null ? ValueWrapper.empty() : type;
            return this;
        }


        public ResourceReferenceDto build() {

            ResourceReferenceDto instance = new ResourceReferenceDto();
            instance.id = id;
            instance.name = name;
            instance.type = type;

            return instance;

        }

    }

    @ProjectionFor(ResourceReferenceDto.class)
    public static class Projection extends DtoProjection<ResourceReferenceDto> {

        public FieldConf id;
        public FieldConf name;
        public FieldConf type;

        public Projection() {
        }

        public void applyProjectionTo(ResourceReferenceDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
            dto.type = ProjectionUtils.getProjectedValue(dto.type, this.type);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(name, that.name)
                       && Objects.equals(type, that.type);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    name,
                    type);
        }

        public static class Builder {

            FieldConf id;
            FieldConf name;
            FieldConf type;

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

            public Builder type(final FieldConf type){
                this.type = type;
                return this;
            }

            public Builder type(final Presence presence){
                type = FieldConf.of(presence);
                return this;
            }

            public ResourceReferenceDto.Projection build() {

                ResourceReferenceDto.Projection instance = new ResourceReferenceDto.Projection();
                instance.id = id;
                instance.name = name;
                instance.type = type;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _name = "name";
        public static final String _type = "type";

    }

}