package solutions.sulfura.hyperkit.dtos;

import solutions.sulfura.hyperkit.examples.model.AuthRole;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(AuthRole.Permission.class)
public class PermissionDto implements Dto<AuthRole.Permission> {

    public ValueWrapper<Long> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();

    public PermissionDto() {
    }

    public Class<AuthRole.Permission> getSourceClass() {
        return AuthRole.Permission.class;
    }

    public static class Builder {

        ValueWrapper<Long> id = ValueWrapper.empty();
        ValueWrapper<String> name = ValueWrapper.empty();

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


        public PermissionDto build() {

            PermissionDto instance = new PermissionDto();
            instance.id = id;
            instance.name = name;

            return instance;

        }

    }

    @ProjectionFor(PermissionDto.class)
    public static class Projection extends DtoProjection<PermissionDto> {

        public FieldConf id;
        public FieldConf name;

        public Projection() {
        }

        public void applyProjectionTo(PermissionDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(name, that.name);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    name);
        }

        public static class Builder {

            FieldConf id;
            FieldConf name;

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

            public PermissionDto.Projection build() {

                PermissionDto.Projection instance = new PermissionDto.Projection();
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