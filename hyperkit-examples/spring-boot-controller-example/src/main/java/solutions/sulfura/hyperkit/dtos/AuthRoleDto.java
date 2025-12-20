package solutions.sulfura.hyperkit.dtos;

import java.util.Set;

import solutions.sulfura.hyperkit.examples.model.AuthRole;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(AuthRole.class)
public class AuthRoleDto implements Dto<AuthRole> {

    public ValueWrapper<Long> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();
    public ValueWrapper<Set<ListOperation<PermissionDto>>> permissions = ValueWrapper.empty();

    public AuthRoleDto() {
    }

    public Class<AuthRole> getSourceClass() {
        return AuthRole.class;
    }

    public static class Builder {

        ValueWrapper<Long> id = ValueWrapper.empty();
        ValueWrapper<String> name = ValueWrapper.empty();
        ValueWrapper<Set<ListOperation<PermissionDto>>> permissions = ValueWrapper.empty();

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

        public Builder permissions(final ValueWrapper<Set<ListOperation<PermissionDto>>> permissions){
            this.permissions = permissions == null ? ValueWrapper.empty() : permissions;
            return this;
        }


        public AuthRoleDto build() {

            AuthRoleDto instance = new AuthRoleDto();
            instance.id = id;
            instance.name = name;
            instance.permissions = permissions;

            return instance;

        }

    }

    @ProjectionFor(AuthRoleDto.class)
    public static class Projection extends DtoProjection<AuthRoleDto> {

        public FieldConf id;
        public FieldConf name;
        public DtoListFieldConf<PermissionDto.Projection> permissions;

        public Projection() {
        }

        public void applyProjectionTo(AuthRoleDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
            dto.permissions = ProjectionUtils.getProjectedValue(dto.permissions, this.permissions);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(name, that.name)
                       && Objects.equals(permissions, that.permissions);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    name,
                    permissions);
        }

        public static class Builder {

            FieldConf id;
            FieldConf name;
            DtoListFieldConf<PermissionDto.Projection> permissions;

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

            public Builder permissions(final DtoListFieldConf<PermissionDto.Projection> permissions){
                this.permissions = permissions;
                return this;
            }

            public Builder permissions(final Presence presence, final PermissionDto.Projection projection){
                permissions = DtoListFieldConf.of(presence, projection);
                return this;
            }

            public AuthRoleDto.Projection build() {

                AuthRoleDto.Projection instance = new AuthRoleDto.Projection();
                instance.id = id;
                instance.name = name;
                instance.permissions = permissions;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _name = "name";
        public static final String _permissions = "permissions";

    }

}