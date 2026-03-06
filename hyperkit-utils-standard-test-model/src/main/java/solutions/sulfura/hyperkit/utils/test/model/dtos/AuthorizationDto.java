package solutions.sulfura.hyperkit.utils.test.model.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import java.util.Set;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.utils.test.model.dtos.ResourceReferenceDto;
import solutions.sulfura.hyperkit.utils.test.model.model.iam.Authorization;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.utils.test.model.dtos.RoleDto;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(Authorization.class)
public class AuthorizationDto implements Dto<Authorization> {

    public ValueWrapper<String> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();
    public ValueWrapper<Set<ListOperation<ResourceReferenceDto>>> resourceReferences = ValueWrapper.empty();
    public ValueWrapper<RoleDto> role = ValueWrapper.empty();

    public AuthorizationDto() {
    }

    public Class<Authorization> getSourceClass() {
        return Authorization.class;
    }

    public static class Builder {

        ValueWrapper<String> id = ValueWrapper.empty();
        ValueWrapper<String> name = ValueWrapper.empty();
        ValueWrapper<Set<ListOperation<ResourceReferenceDto>>> resourceReferences = ValueWrapper.empty();
        ValueWrapper<RoleDto> role = ValueWrapper.empty();

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

        public Builder resourceReferences(final ValueWrapper<Set<ListOperation<ResourceReferenceDto>>> resourceReferences){
            this.resourceReferences = resourceReferences == null ? ValueWrapper.empty() : resourceReferences;
            return this;
        }

        public Builder role(final ValueWrapper<RoleDto> role){
            this.role = role == null ? ValueWrapper.empty() : role;
            return this;
        }


        public AuthorizationDto build() {

            AuthorizationDto instance = new AuthorizationDto();
            instance.id = id;
            instance.name = name;
            instance.resourceReferences = resourceReferences;
            instance.role = role;

            return instance;

        }

    }

    @ProjectionFor(AuthorizationDto.class)
    public static class Projection extends DtoProjection<AuthorizationDto> {

        public FieldConf id;
        public FieldConf name;
        public DtoListFieldConf<ResourceReferenceDto.Projection> resourceReferences;
        public DtoFieldConf<RoleDto.Projection> role;

        public Projection() {
        }

        public void applyProjectionTo(AuthorizationDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
            dto.resourceReferences = ProjectionUtils.getProjectedValue(dto.resourceReferences, this.resourceReferences);
            dto.role = ProjectionUtils.getProjectedValue(dto.role, this.role);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(name, that.name)
                       && Objects.equals(resourceReferences, that.resourceReferences)
                       && Objects.equals(role, that.role);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    name,
                    resourceReferences,
                    role);
        }

        public static class Builder {

            FieldConf id;
            FieldConf name;
            DtoListFieldConf<ResourceReferenceDto.Projection> resourceReferences;
            DtoFieldConf<RoleDto.Projection> role;

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

            public Builder resourceReferences(final DtoListFieldConf<ResourceReferenceDto.Projection> resourceReferences){
                this.resourceReferences = resourceReferences;
                return this;
            }

            public Builder resourceReferences(final Presence presence, final ResourceReferenceDto.Projection projection){
                resourceReferences = DtoListFieldConf.of(presence, projection);
                return this;
            }

            public Builder role(final DtoFieldConf<RoleDto.Projection> role){
                this.role = role;
                return this;
            }

            public Builder role(final Presence presence, final RoleDto.Projection projection){
                role = DtoFieldConf.of(presence, projection);
                return this;
            }

            public AuthorizationDto.Projection build() {

                AuthorizationDto.Projection instance = new AuthorizationDto.Projection();
                instance.id = id;
                instance.name = name;
                instance.resourceReferences = resourceReferences;
                instance.role = role;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _name = "name";
        public static final String _resourceReferences = "resourceReferences";
        public static final String _role = "role";

    }

}