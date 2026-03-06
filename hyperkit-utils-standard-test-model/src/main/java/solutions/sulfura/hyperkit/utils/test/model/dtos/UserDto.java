package solutions.sulfura.hyperkit.utils.test.model.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.utils.test.model.dtos.AuthorizationDto;
import java.util.Set;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.test.model.model.iam.User;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.utils.test.model.dtos.AccountDto;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(User.class)
public class UserDto implements Dto<User> {

    public ValueWrapper<String> id = ValueWrapper.empty();
    public ValueWrapper<String> username = ValueWrapper.empty();
    public ValueWrapper<String> email = ValueWrapper.empty();
    public ValueWrapper<Set<ListOperation<AuthorizationDto>>> authorizations = ValueWrapper.empty();
    public ValueWrapper<Set<ListOperation<AccountDto>>> account = ValueWrapper.empty();

    public UserDto() {
    }

    public Class<User> getSourceClass() {
        return User.class;
    }

    public static class Builder {

        ValueWrapper<String> id = ValueWrapper.empty();
        ValueWrapper<String> username = ValueWrapper.empty();
        ValueWrapper<String> email = ValueWrapper.empty();
        ValueWrapper<Set<ListOperation<AuthorizationDto>>> authorizations = ValueWrapper.empty();
        ValueWrapper<Set<ListOperation<AccountDto>>> account = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(final ValueWrapper<String> id){
            this.id = id == null ? ValueWrapper.empty() : id;
            return this;
        }

        public Builder username(final ValueWrapper<String> username){
            this.username = username == null ? ValueWrapper.empty() : username;
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

        public Builder account(final ValueWrapper<Set<ListOperation<AccountDto>>> account){
            this.account = account == null ? ValueWrapper.empty() : account;
            return this;
        }


        public UserDto build() {

            UserDto instance = new UserDto();
            instance.id = id;
            instance.username = username;
            instance.email = email;
            instance.authorizations = authorizations;
            instance.account = account;

            return instance;

        }

    }

    @ProjectionFor(UserDto.class)
    public static class Projection extends DtoProjection<UserDto> {

        public FieldConf id;
        public FieldConf username;
        public FieldConf email;
        public DtoListFieldConf<AccountDto.Projection> authorizations;
        public DtoListFieldConf<AccountDto.Projection> account;

        public Projection() {
        }

        public void applyProjectionTo(UserDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.username = ProjectionUtils.getProjectedValue(dto.username, this.username);
            dto.email = ProjectionUtils.getProjectedValue(dto.email, this.email);
            dto.authorizations = ProjectionUtils.getProjectedValue(dto.authorizations, this.authorizations);
            dto.account = ProjectionUtils.getProjectedValue(dto.account, this.account);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(username, that.username)
                       && Objects.equals(email, that.email)
                       && Objects.equals(authorizations, that.authorizations)
                       && Objects.equals(account, that.account);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    username,
                    email,
                    authorizations,
                    account);
        }

        public static class Builder {

            FieldConf id;
            FieldConf username;
            FieldConf email;
            DtoListFieldConf<AccountDto.Projection> authorizations;
            DtoListFieldConf<AccountDto.Projection> account;

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

            public Builder username(final FieldConf username){
                this.username = username;
                return this;
            }

            public Builder username(final Presence presence){
                username = FieldConf.of(presence);
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

            public Builder authorizations(final DtoListFieldConf<AccountDto.Projection> authorizations){
                this.authorizations = authorizations;
                return this;
            }

            public Builder authorizations(final Presence presence, final AccountDto.Projection projection){
                authorizations = DtoListFieldConf.of(presence, projection);
                return this;
            }

            public Builder account(final DtoListFieldConf<AccountDto.Projection> account){
                this.account = account;
                return this;
            }

            public Builder account(final Presence presence, final AccountDto.Projection projection){
                account = DtoListFieldConf.of(presence, projection);
                return this;
            }

            public UserDto.Projection build() {

                UserDto.Projection instance = new UserDto.Projection();
                instance.id = id;
                instance.username = username;
                instance.email = email;
                instance.authorizations = authorizations;
                instance.account = account;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _username = "username";
        public static final String _email = "email";
        public static final String _authorizations = "authorizations";
        public static final String _account = "account";

    }

}