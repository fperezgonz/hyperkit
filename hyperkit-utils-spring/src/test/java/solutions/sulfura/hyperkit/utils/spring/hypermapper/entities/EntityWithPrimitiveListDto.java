package solutions.sulfura.hyperkit.utils.spring.hypermapper.entities;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.ListFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;

import java.util.List;

@DtoFor(EntityWithPrimitiveList.class)
public class EntityWithPrimitiveListDto implements Dto<EntityWithPrimitiveList> {

    public ValueWrapper<Long> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();
    public ValueWrapper<List<ListOperation<String>>> stringList = ValueWrapper.empty();
    public ValueWrapper<List<ListOperation<Integer>>> integerList = ValueWrapper.empty();

    public EntityWithPrimitiveListDto() {
    }

    public Class<EntityWithPrimitiveList> getSourceClass() {
        return EntityWithPrimitiveList.class;
    }

    @SuppressWarnings("unused")
    public static class Builder {
        ValueWrapper<Long> id = ValueWrapper.empty();
        ValueWrapper<String> name = ValueWrapper.empty();
        ValueWrapper<List<ListOperation<String>>> stringList = ValueWrapper.empty();
        ValueWrapper<List<ListOperation<Integer>>> integerList = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(final ValueWrapper<Long> id) {
            this.id = id == null ? ValueWrapper.empty() : id;
            return this;
        }

        public Builder name(final ValueWrapper<String> name) {
            this.name = name == null ? ValueWrapper.empty() : name;
            return this;
        }

        public Builder stringList(final ValueWrapper<List<ListOperation<String>>> stringList) {
            this.stringList = stringList == null ? ValueWrapper.empty() : stringList;
            return this;
        }

        public Builder integerList(final ValueWrapper<List<ListOperation<Integer>>> integerList) {
            this.integerList = integerList == null ? ValueWrapper.empty() : integerList;
            return this;
        }

        public EntityWithPrimitiveListDto build() {
            EntityWithPrimitiveListDto instance = new EntityWithPrimitiveListDto();
            instance.id = id;
            instance.name = name;
            instance.stringList = stringList;
            instance.integerList = integerList;

            return instance;
        }
    }

    @ProjectionFor(EntityWithPrimitiveListDto.class)
    public static class Projection extends DtoProjection<EntityWithPrimitiveListDto> {

        public FieldConf id;
        public FieldConf name;
        public ListFieldConf stringList;
        public ListFieldConf integerList;

        public Projection() {
        }

        public void applyProjectionTo(EntityWithPrimitiveListDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
            dto.stringList = ProjectionUtils.getProjectedValue(dto.stringList, this.stringList);
            dto.integerList = ProjectionUtils.getProjectedValue(dto.integerList, this.integerList);
        }

        @SuppressWarnings("unused")
        public static class Builder {
            FieldConf id;
            FieldConf name;
            ListFieldConf stringList;
            ListFieldConf integerList;

            public static Builder newInstance() {
                return new Builder();
            }

            public Builder id(final FieldConf id) {
                this.id = id;
                return this;
            }

            public Builder id(final Presence presence) {
                id = FieldConf.of(presence);
                return this;
            }

            public Builder name(final FieldConf name) {
                this.name = name;
                return this;
            }

            public Builder name(final Presence presence) {
                name = FieldConf.of(presence);
                return this;
            }

            public Builder stringList(final ListFieldConf stringList) {
                this.stringList = stringList;
                return this;
            }

            public Builder stringList(final Presence presence) {
                stringList = ListFieldConf.of(presence);
                return this;
            }

            public Builder integerList(final ListFieldConf integerList) {
                this.integerList = integerList;
                return this;
            }

            public Builder integerList(final Presence presence) {
                integerList = ListFieldConf.of(presence);
                return this;
            }

            public Projection build() {
                Projection instance = new Projection();
                instance.id = id;
                instance.name = name;
                instance.stringList = stringList;
                instance.integerList = integerList;

                return instance;
            }
        }
    }

    @SuppressWarnings("unused")
    public static class DtoModel {
        public static final String _id = "id";
        public static final String _name = "name";
        public static final String _stringList = "stringList";
        public static final String _integerList = "integerList";
    }
}