package solutions.sulfura.hyperkit.utils.test.model.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import java.util.Set;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.test.model.dtos.ActionDto;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.hyperkit.utils.test.model.model.iam.Role;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(Role.class)
public class RoleDto implements Dto<Role> {

    public ValueWrapper<String> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();
    public ValueWrapper<Set<ListOperation<ActionDto>>> actions = ValueWrapper.empty();

    public RoleDto() {
    }

    public Class<Role> getSourceClass() {
        return Role.class;
    }

    public static class Builder {

        ValueWrapper<String> id = ValueWrapper.empty();
        ValueWrapper<String> name = ValueWrapper.empty();
        ValueWrapper<Set<ListOperation<ActionDto>>> actions = ValueWrapper.empty();

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

        public Builder actions(final ValueWrapper<Set<ListOperation<ActionDto>>> actions){
            this.actions = actions == null ? ValueWrapper.empty() : actions;
            return this;
        }


        public RoleDto build() {

            RoleDto instance = new RoleDto();
            instance.id = id;
            instance.name = name;
            instance.actions = actions;

            return instance;

        }

    }

    @ProjectionFor(RoleDto.class)
    public static class Projection extends DtoProjection<RoleDto> {

        public FieldConf id;
        public FieldConf name;
        public DtoListFieldConf<ActionDto.Projection> actions;

        public Projection() {
        }

        public void applyProjectionTo(RoleDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
            dto.actions = ProjectionUtils.getProjectedValue(dto.actions, this.actions);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return  Objects.equals(id, that.id)
                       && Objects.equals(name, that.name)
                       && Objects.equals(actions, that.actions);

        }

        @Override
        public int hashCode() {
            return Objects.hash(id,
                    name,
                    actions);
        }

        public static class Builder {

            FieldConf id;
            FieldConf name;
            DtoListFieldConf<ActionDto.Projection> actions;

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

            public Builder actions(final DtoListFieldConf<ActionDto.Projection> actions){
                this.actions = actions;
                return this;
            }

            public Builder actions(final Presence presence, final ActionDto.Projection projection){
                actions = DtoListFieldConf.of(presence, projection);
                return this;
            }

            public RoleDto.Projection build() {

                RoleDto.Projection instance = new RoleDto.Projection();
                instance.id = id;
                instance.name = name;
                instance.actions = actions;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _name = "name";
        public static final String _actions = "actions";

    }

}