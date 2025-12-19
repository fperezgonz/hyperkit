package solutions.sulfura.hyperkit.dtos;

import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import solutions.sulfura.hyperkit.examples.app.User;

import java.util.Objects;
import java.util.Set;

@DtoFor(User.class)
public class UserDto implements Dto<User> {

    public ValueWrapper<Long> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();
    public ValueWrapper<String> email = ValueWrapper.empty();
    public ValueWrapper<Set<ListOperation<AuthRoleDto>>> roles = ValueWrapper.empty();

    public UserDto() {
    }

    public Class<User> getSourceClass() {
        return User.class;
    }

    public static class Builder {

        ValueWrapper<Long> id = ValueWrapper.empty();
        ValueWrapper<String> name = ValueWrapper.empty();
        ValueWrapper<String> email = ValueWrapper.empty();
        ValueWrapper<Set<ListOperation<AuthRoleDto>>> roles = ValueWrapper.empty();

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

        public Builder email(final ValueWrapper<String> email){
            this.email = email == null ? ValueWrapper.empty() : email;
            return this;
        }

        public Builder roles(final ValueWrapper<Set<ListOperation<AuthRoleDto>>> roles){
            this.roles = roles == null ? ValueWrapper.empty() : roles;
            return this;
        }


        public UserDto build() {

            UserDto instance = new UserDto();
            instance.id = id;
            instance.name = name;
            instance.email = email;
            instance.roles = roles;

            return instance;

        }

    }

    @ProjectionFor(UserDto.class)
    public static class Projection extends DtoProjection<UserDto> {

        public FieldConf id;
        public FieldConf name;
        public FieldConf email;
        public DtoListFieldConf<AuthRoleDto.Projection> roles;

        public Projection() {
        }

        public void applyProjectionTo(UserDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
            dto.email = ProjectionUtils.getProjectedValue(dto.email, this.email);
            dto.roles = ProjectionUtils.getProjectedValue(dto.roles, this.roles);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(name, that.name)
                       && Objects.equals(email, that.email)
                       && Objects.equals(roles, that.roles);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    name,
                    email,
                    roles);
        }

        public static class Builder {

            FieldConf id;
            FieldConf name;
            FieldConf email;
            DtoListFieldConf<AuthRoleDto.Projection> roles;

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

            public Builder email(final FieldConf email){
                this.email = email;
                return this;
            }

            public Builder email(final Presence presence){
                email = FieldConf.of(presence);
                return this;
            }

            public Builder roles(final DtoListFieldConf<AuthRoleDto.Projection> roles){
                this.roles = roles;
                return this;
            }

            public Builder roles(final Presence presence, final AuthRoleDto.Projection projection){
                roles = DtoListFieldConf.of(presence, projection);
                return this;
            }

            public UserDto.Projection build() {

                UserDto.Projection instance = new UserDto.Projection();
                instance.id = id;
                instance.name = name;
                instance.email = email;
                instance.roles = roles;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _name = "name";
        public static final String _email = "email";
        public static final String _roles = "roles";

    }

}