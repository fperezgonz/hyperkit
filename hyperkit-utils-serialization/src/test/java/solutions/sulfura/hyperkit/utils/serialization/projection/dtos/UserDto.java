package solutions.sulfura.hyperkit.utils.serialization.projection.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.utils.serialization.projection.dtos.AuthorizationDto;
import java.util.Set;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.serialization.projection.model.User;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(User.class)
public class UserDto implements Dto<User> {

    public ValueWrapper<String> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();
    public ValueWrapper<String> email = ValueWrapper.empty();
    public ValueWrapper<Set<ListOperation<AuthorizationDto>>> authorizations = ValueWrapper.empty();

    public UserDto() {
    }

    public Class<User> getSourceClass() {
        return User.class;
    }

    public static class Builder {

        ValueWrapper<String> id = ValueWrapper.empty();
        ValueWrapper<String> name = ValueWrapper.empty();
        ValueWrapper<String> email = ValueWrapper.empty();
        ValueWrapper<Set<ListOperation<AuthorizationDto>>> authorizations = ValueWrapper.empty();

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

        public Builder email(final ValueWrapper<String> email){
            this.email = email == null ? ValueWrapper.empty() : email;
            return this;
        }

        public Builder authorizations(final ValueWrapper<Set<ListOperation<AuthorizationDto>>> authorizations){
            this.authorizations = authorizations == null ? ValueWrapper.empty() : authorizations;
            return this;
        }


        public UserDto build() {

            UserDto instance = new UserDto();
            instance.id = id;
            instance.name = name;
            instance.email = email;
            instance.authorizations = authorizations;

            return instance;

        }

    }

    @ProjectionFor(UserDto.class)
    public static class Projection extends DtoProjection<UserDto> {

        public FieldConf id;
        public FieldConf name;
        public FieldConf email;
        public DtoListFieldConf<AuthorizationDto.Projection> authorizations;

        public Projection() {
        }

        public void applyProjectionTo(UserDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
            dto.email = ProjectionUtils.getProjectedValue(dto.email, this.email);
            dto.authorizations = ProjectionUtils.getProjectedValue(dto.authorizations, this.authorizations);
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
                       && Objects.equals(authorizations, that.authorizations);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    name,
                    email,
                    authorizations);
        }

        public static class Builder {

            FieldConf id;
            FieldConf name;
            FieldConf email;
            DtoListFieldConf<AuthorizationDto.Projection> authorizations;

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

            public Builder authorizations(final DtoListFieldConf<AuthorizationDto.Projection> authorizations){
                this.authorizations = authorizations;
                return this;
            }

            public Builder authorizations(final Presence presence, final AuthorizationDto.Projection projection){
                authorizations = DtoListFieldConf.of(presence, projection);
                return this;
            }

            public UserDto.Projection build() {

                UserDto.Projection instance = new UserDto.Projection();
                instance.id = id;
                instance.name = name;
                instance.email = email;
                instance.authorizations = authorizations;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _name = "name";
        public static final String _email = "email";
        public static final String _authorizations = "authorizations";

    }

}