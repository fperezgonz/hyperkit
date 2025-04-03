package solutions.sulfura.hyperkit.dtos.generics.inheritance;

import solutions.sulfura.hyperkit.dtos.Dto;
import java.util.Set;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.generics.inheritance.GenericChildClassWithParameterizedType;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.projection.fields.ListFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;

@DtoFor(GenericChildClassWithParameterizedType.class)
public class GenericChildClassWithParameterizedTypeDto implements Dto<GenericChildClassWithParameterizedType> {

    public ValueWrapper<String> genericProperty = ValueWrapper.empty();
    public ValueWrapper<Set<ListOperation<String>>> nestedGenericProperty = ValueWrapper.empty();
    public ValueWrapper<String> genericPropertyWithGetter = ValueWrapper.empty();
    public ValueWrapper<String> genericPropertyWithSetter = ValueWrapper.empty();
    public ValueWrapper<String> inheritedGenericProperty = ValueWrapper.empty();
    public ValueWrapper<Set<ListOperation<String>>> inheritedNestedGenericProperty = ValueWrapper.empty();
    public ValueWrapper<String> overlappingGenericProperty = ValueWrapper.empty();
    public ValueWrapper<Set<ListOperation<String>>> overlappingNestedGenericProperty = ValueWrapper.empty();
    public ValueWrapper<String> inheritedGenericPropertyWithGetter = ValueWrapper.empty();
    public ValueWrapper<String> inheritedGenericPropertyWithSetter = ValueWrapper.empty();

    public GenericChildClassWithParameterizedTypeDto() {
    }

    public Class<GenericChildClassWithParameterizedType> getSourceClass() {
        return GenericChildClassWithParameterizedType.class;
    }

    public static class Builder {

        ValueWrapper<String> genericProperty = ValueWrapper.empty();
        ValueWrapper<Set<ListOperation<String>>> nestedGenericProperty = ValueWrapper.empty();
        ValueWrapper<String> genericPropertyWithGetter = ValueWrapper.empty();
        ValueWrapper<String> genericPropertyWithSetter = ValueWrapper.empty();
        ValueWrapper<String> inheritedGenericProperty = ValueWrapper.empty();
        ValueWrapper<Set<ListOperation<String>>> inheritedNestedGenericProperty = ValueWrapper.empty();
        ValueWrapper<String> overlappingGenericProperty = ValueWrapper.empty();
        ValueWrapper<Set<ListOperation<String>>> overlappingNestedGenericProperty = ValueWrapper.empty();
        ValueWrapper<String> inheritedGenericPropertyWithGetter = ValueWrapper.empty();
        ValueWrapper<String> inheritedGenericPropertyWithSetter = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder genericProperty(final ValueWrapper<String> genericProperty){
            this.genericProperty = genericProperty == null ? ValueWrapper.empty() : genericProperty;
            return this;
        }

        public Builder nestedGenericProperty(final ValueWrapper<Set<ListOperation<String>>> nestedGenericProperty){
            this.nestedGenericProperty = nestedGenericProperty == null ? ValueWrapper.empty() : nestedGenericProperty;
            return this;
        }

        public Builder genericPropertyWithGetter(final ValueWrapper<String> genericPropertyWithGetter){
            this.genericPropertyWithGetter = genericPropertyWithGetter == null ? ValueWrapper.empty() : genericPropertyWithGetter;
            return this;
        }

        public Builder genericPropertyWithSetter(final ValueWrapper<String> genericPropertyWithSetter){
            this.genericPropertyWithSetter = genericPropertyWithSetter == null ? ValueWrapper.empty() : genericPropertyWithSetter;
            return this;
        }

        public Builder inheritedGenericProperty(final ValueWrapper<String> inheritedGenericProperty){
            this.inheritedGenericProperty = inheritedGenericProperty == null ? ValueWrapper.empty() : inheritedGenericProperty;
            return this;
        }

        public Builder inheritedNestedGenericProperty(final ValueWrapper<Set<ListOperation<String>>> inheritedNestedGenericProperty){
            this.inheritedNestedGenericProperty = inheritedNestedGenericProperty == null ? ValueWrapper.empty() : inheritedNestedGenericProperty;
            return this;
        }

        public Builder overlappingGenericProperty(final ValueWrapper<String> overlappingGenericProperty){
            this.overlappingGenericProperty = overlappingGenericProperty == null ? ValueWrapper.empty() : overlappingGenericProperty;
            return this;
        }

        public Builder overlappingNestedGenericProperty(final ValueWrapper<Set<ListOperation<String>>> overlappingNestedGenericProperty){
            this.overlappingNestedGenericProperty = overlappingNestedGenericProperty == null ? ValueWrapper.empty() : overlappingNestedGenericProperty;
            return this;
        }

        public Builder inheritedGenericPropertyWithGetter(final ValueWrapper<String> inheritedGenericPropertyWithGetter){
            this.inheritedGenericPropertyWithGetter = inheritedGenericPropertyWithGetter == null ? ValueWrapper.empty() : inheritedGenericPropertyWithGetter;
            return this;
        }

        public Builder inheritedGenericPropertyWithSetter(final ValueWrapper<String> inheritedGenericPropertyWithSetter){
            this.inheritedGenericPropertyWithSetter = inheritedGenericPropertyWithSetter == null ? ValueWrapper.empty() : inheritedGenericPropertyWithSetter;
            return this;
        }


        public GenericChildClassWithParameterizedTypeDto build() {

            GenericChildClassWithParameterizedTypeDto instance = new GenericChildClassWithParameterizedTypeDto();
            instance.genericProperty = genericProperty;
            instance.nestedGenericProperty = nestedGenericProperty;
            instance.genericPropertyWithGetter = genericPropertyWithGetter;
            instance.genericPropertyWithSetter = genericPropertyWithSetter;
            instance.inheritedGenericProperty = inheritedGenericProperty;
            instance.inheritedNestedGenericProperty = inheritedNestedGenericProperty;
            instance.overlappingGenericProperty = overlappingGenericProperty;
            instance.overlappingNestedGenericProperty = overlappingNestedGenericProperty;
            instance.inheritedGenericPropertyWithGetter = inheritedGenericPropertyWithGetter;
            instance.inheritedGenericPropertyWithSetter = inheritedGenericPropertyWithSetter;

            return instance;

        }

    }

    @ProjectionFor(GenericChildClassWithParameterizedTypeDto.class)
    public static class Projection extends DtoProjection<GenericChildClassWithParameterizedTypeDto> {

        public FieldConf genericProperty;
        public ListFieldConf nestedGenericProperty;
        public FieldConf genericPropertyWithGetter;
        public FieldConf genericPropertyWithSetter;
        public FieldConf inheritedGenericProperty;
        public ListFieldConf inheritedNestedGenericProperty;
        public FieldConf overlappingGenericProperty;
        public ListFieldConf overlappingNestedGenericProperty;
        public FieldConf inheritedGenericPropertyWithGetter;
        public FieldConf inheritedGenericPropertyWithSetter;

        public Projection() {
        }

        public void applyProjectionTo(GenericChildClassWithParameterizedTypeDto dto) throws DtoProjectionException {
            dto.genericProperty = ProjectionUtils.getProjectedValue(dto.genericProperty, this.genericProperty);
            dto.nestedGenericProperty = ProjectionUtils.getProjectedValue(dto.nestedGenericProperty, this.nestedGenericProperty);
            dto.genericPropertyWithGetter = ProjectionUtils.getProjectedValue(dto.genericPropertyWithGetter, this.genericPropertyWithGetter);
            dto.genericPropertyWithSetter = ProjectionUtils.getProjectedValue(dto.genericPropertyWithSetter, this.genericPropertyWithSetter);
            dto.inheritedGenericProperty = ProjectionUtils.getProjectedValue(dto.inheritedGenericProperty, this.inheritedGenericProperty);
            dto.inheritedNestedGenericProperty = ProjectionUtils.getProjectedValue(dto.inheritedNestedGenericProperty, this.inheritedNestedGenericProperty);
            dto.overlappingGenericProperty = ProjectionUtils.getProjectedValue(dto.overlappingGenericProperty, this.overlappingGenericProperty);
            dto.overlappingNestedGenericProperty = ProjectionUtils.getProjectedValue(dto.overlappingNestedGenericProperty, this.overlappingNestedGenericProperty);
            dto.inheritedGenericPropertyWithGetter = ProjectionUtils.getProjectedValue(dto.inheritedGenericPropertyWithGetter, this.inheritedGenericPropertyWithGetter);
            dto.inheritedGenericPropertyWithSetter = ProjectionUtils.getProjectedValue(dto.inheritedGenericPropertyWithSetter, this.inheritedGenericPropertyWithSetter);
        }

        public static class Builder {

            FieldConf genericProperty;
            ListFieldConf nestedGenericProperty;
            FieldConf genericPropertyWithGetter;
            FieldConf genericPropertyWithSetter;
            FieldConf inheritedGenericProperty;
            ListFieldConf inheritedNestedGenericProperty;
            FieldConf overlappingGenericProperty;
            ListFieldConf overlappingNestedGenericProperty;
            FieldConf inheritedGenericPropertyWithGetter;
            FieldConf inheritedGenericPropertyWithSetter;

            public static Builder newInstance() {
                return new Builder();
            }

            public Builder genericProperty(final FieldConf genericProperty){
                this.genericProperty = genericProperty;
                return this;
            }

            public Builder genericProperty(final Presence presence){
                genericProperty = FieldConf.of(presence);
                return this;
            }

            public Builder nestedGenericProperty(final ListFieldConf nestedGenericProperty){
                this.nestedGenericProperty = nestedGenericProperty;
                return this;
            }

            public Builder nestedGenericProperty(final Presence presence){
                nestedGenericProperty = ListFieldConf.of(presence);
                return this;
            }

            public Builder genericPropertyWithGetter(final FieldConf genericPropertyWithGetter){
                this.genericPropertyWithGetter = genericPropertyWithGetter;
                return this;
            }

            public Builder genericPropertyWithGetter(final Presence presence){
                genericPropertyWithGetter = FieldConf.of(presence);
                return this;
            }

            public Builder genericPropertyWithSetter(final FieldConf genericPropertyWithSetter){
                this.genericPropertyWithSetter = genericPropertyWithSetter;
                return this;
            }

            public Builder genericPropertyWithSetter(final Presence presence){
                genericPropertyWithSetter = FieldConf.of(presence);
                return this;
            }

            public Builder inheritedGenericProperty(final FieldConf inheritedGenericProperty){
                this.inheritedGenericProperty = inheritedGenericProperty;
                return this;
            }

            public Builder inheritedGenericProperty(final Presence presence){
                inheritedGenericProperty = FieldConf.of(presence);
                return this;
            }

            public Builder inheritedNestedGenericProperty(final ListFieldConf inheritedNestedGenericProperty){
                this.inheritedNestedGenericProperty = inheritedNestedGenericProperty;
                return this;
            }

            public Builder inheritedNestedGenericProperty(final Presence presence){
                inheritedNestedGenericProperty = ListFieldConf.of(presence);
                return this;
            }

            public Builder overlappingGenericProperty(final FieldConf overlappingGenericProperty){
                this.overlappingGenericProperty = overlappingGenericProperty;
                return this;
            }

            public Builder overlappingGenericProperty(final Presence presence){
                overlappingGenericProperty = FieldConf.of(presence);
                return this;
            }

            public Builder overlappingNestedGenericProperty(final ListFieldConf overlappingNestedGenericProperty){
                this.overlappingNestedGenericProperty = overlappingNestedGenericProperty;
                return this;
            }

            public Builder overlappingNestedGenericProperty(final Presence presence){
                overlappingNestedGenericProperty = ListFieldConf.of(presence);
                return this;
            }

            public Builder inheritedGenericPropertyWithGetter(final FieldConf inheritedGenericPropertyWithGetter){
                this.inheritedGenericPropertyWithGetter = inheritedGenericPropertyWithGetter;
                return this;
            }

            public Builder inheritedGenericPropertyWithGetter(final Presence presence){
                inheritedGenericPropertyWithGetter = FieldConf.of(presence);
                return this;
            }

            public Builder inheritedGenericPropertyWithSetter(final FieldConf inheritedGenericPropertyWithSetter){
                this.inheritedGenericPropertyWithSetter = inheritedGenericPropertyWithSetter;
                return this;
            }

            public Builder inheritedGenericPropertyWithSetter(final Presence presence){
                inheritedGenericPropertyWithSetter = FieldConf.of(presence);
                return this;
            }

            public GenericChildClassWithParameterizedTypeDto.Projection build() {

                GenericChildClassWithParameterizedTypeDto.Projection instance = new GenericChildClassWithParameterizedTypeDto.Projection();
                instance.genericProperty = genericProperty;
                instance.nestedGenericProperty = nestedGenericProperty;
                instance.genericPropertyWithGetter = genericPropertyWithGetter;
                instance.genericPropertyWithSetter = genericPropertyWithSetter;
                instance.inheritedGenericProperty = inheritedGenericProperty;
                instance.inheritedNestedGenericProperty = inheritedNestedGenericProperty;
                instance.overlappingGenericProperty = overlappingGenericProperty;
                instance.overlappingNestedGenericProperty = overlappingNestedGenericProperty;
                instance.inheritedGenericPropertyWithGetter = inheritedGenericPropertyWithGetter;
                instance.inheritedGenericPropertyWithSetter = inheritedGenericPropertyWithSetter;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _genericProperty = "genericProperty";
        public static final String _nestedGenericProperty = "nestedGenericProperty";
        public static final String _genericPropertyWithGetter = "genericPropertyWithGetter";
        public static final String _genericPropertyWithSetter = "genericPropertyWithSetter";
        public static final String _inheritedGenericProperty = "inheritedGenericProperty";
        public static final String _inheritedNestedGenericProperty = "inheritedNestedGenericProperty";
        public static final String _overlappingGenericProperty = "overlappingGenericProperty";
        public static final String _overlappingNestedGenericProperty = "overlappingNestedGenericProperty";
        public static final String _inheritedGenericPropertyWithGetter = "inheritedGenericPropertyWithGetter";
        public static final String _inheritedGenericPropertyWithSetter = "inheritedGenericPropertyWithSetter";

    }

}