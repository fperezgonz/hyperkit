package solutions.sulfura.hyperkit.utils.test.model.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.utils.test.model.model.Account;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.test.model.dtos.UserDto;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(Account.class)
public class AccountDto implements Dto<Account> {

    public ValueWrapper<String> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();
    public ValueWrapper<UserDto> user = ValueWrapper.empty();

    public AccountDto() {
    }

    public Class<Account> getSourceClass() {
        return Account.class;
    }

    public static class Builder {

        ValueWrapper<String> id = ValueWrapper.empty();
        ValueWrapper<String> name = ValueWrapper.empty();
        ValueWrapper<UserDto> user = ValueWrapper.empty();

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

        public Builder user(final ValueWrapper<UserDto> user){
            this.user = user == null ? ValueWrapper.empty() : user;
            return this;
        }


        public AccountDto build() {

            AccountDto instance = new AccountDto();
            instance.id = id;
            instance.name = name;
            instance.user = user;

            return instance;

        }

    }

    @ProjectionFor(AccountDto.class)
    public static class Projection extends DtoProjection<AccountDto> {

        public FieldConf id;
        public FieldConf name;
        public DtoFieldConf<UserDto.Projection> user;

        public Projection() {
        }

        public void applyProjectionTo(AccountDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
            dto.user = ProjectionUtils.getProjectedValue(dto.user, this.user);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(name, that.name)
                       && Objects.equals(user, that.user);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    name,
                    user);
        }

        public static class Builder {

            FieldConf id;
            FieldConf name;
            DtoFieldConf<UserDto.Projection> user;

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

            public Builder user(final DtoFieldConf<UserDto.Projection> user){
                this.user = user;
                return this;
            }

            public Builder user(final Presence presence, final UserDto.Projection projection){
                user = DtoFieldConf.of(presence, projection);
                return this;
            }

            public AccountDto.Projection build() {

                AccountDto.Projection instance = new AccountDto.Projection();
                instance.id = id;
                instance.name = name;
                instance.user = user;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _name = "name";
        public static final String _user = "user";

    }

}