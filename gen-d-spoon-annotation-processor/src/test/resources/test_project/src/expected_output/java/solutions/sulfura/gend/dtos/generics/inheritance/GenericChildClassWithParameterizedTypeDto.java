package solutions.sulfura.gend.dtos.generics.inheritance;

import solutions.sulfura.gend.dtos.generics.inheritance.GenericChildClassWithParameterizedTypeDto.Builder;
import solutions.sulfura.gend.dtos.Dto;
import java.util.Set;
import solutions.sulfura.gend.dtos.generics.inheritance.GenericChildClassWithParameterizedType;
import solutions.sulfura.gend.dtos.generics.inheritance.GenericChildClassWithParameterizedTypeDto;
import solutions.sulfura.gend.dtos.projection.fields.FieldConf;
import solutions.sulfura.gend.dtos.projection.DtoProjectionException;
import solutions.sulfura.gend.dtos.projection.DtoProjection;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.generics.inheritance.GenericChildClassWithParameterizedTypeDto.Projection;
import solutions.sulfura.gend.dtos.generics.inheritance.GenericChildClassWithParameterizedTypeDto.DtoModel;
import solutions.sulfura.gend.dtos.projection.ProjectionFor;
import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.ListOperation;
import solutions.sulfura.gend.dtos.projection.fields.ListFieldConf;
import solutions.sulfura.gend.dtos.projection.ProjectionUtils;

@DtoFor(GenericChildClassWithParameterizedType.class)
public class GenericChildClassWithParameterizedTypeDto implements Dto<GenericChildClassWithParameterizedType>{

    public Option<String> genericProperty = Option.none();
    public Option<Set<ListOperation<String>>> nestedGenericProperty = Option.none();
    public Option<String> genericPropertyWithGetter = Option.none();
    public Option<String> genericPropertyWithSetter = Option.none();
    public Option<String> inheritedGenericProperty = Option.none();
    public Option<Set<ListOperation<String>>> inheritedNestedGenericProperty = Option.none();
    public Option<String> overlappingGenericProperty = Option.none();
    public Option<Set<ListOperation<String>>> overlappingNestedGenericProperty = Option.none();
    public Option<String> inheritedGenericPropertyWithGetter = Option.none();
    public Option<String> inheritedGenericPropertyWithSetter = Option.none();

    public GenericChildClassWithParameterizedTypeDto() {
    }

    public Class<GenericChildClassWithParameterizedType> getSourceClass() {
        return GenericChildClassWithParameterizedType.class;
    }

    public static class Builder{

        Option<String> genericProperty = Option.none();
        Option<Set<ListOperation<String>>> nestedGenericProperty = Option.none();
        Option<String> genericPropertyWithGetter = Option.none();
        Option<String> genericPropertyWithSetter = Option.none();
        Option<String> inheritedGenericProperty = Option.none();
        Option<Set<ListOperation<String>>> inheritedNestedGenericProperty = Option.none();
        Option<String> overlappingGenericProperty = Option.none();
        Option<Set<ListOperation<String>>> overlappingNestedGenericProperty = Option.none();
        Option<String> inheritedGenericPropertyWithGetter = Option.none();
        Option<String> inheritedGenericPropertyWithSetter = Option.none();

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder genericProperty(final Option<String> genericProperty){
            this.genericProperty = genericProperty == null ? Option.none() : genericProperty;
            return this;
        }

        public Builder nestedGenericProperty(final Option<Set<ListOperation<String>>> nestedGenericProperty){
            this.nestedGenericProperty = nestedGenericProperty == null ? Option.none() : nestedGenericProperty;
            return this;
        }

        public Builder genericPropertyWithGetter(final Option<String> genericPropertyWithGetter){
            this.genericPropertyWithGetter = genericPropertyWithGetter == null ? Option.none() : genericPropertyWithGetter;
            return this;
        }

        public Builder genericPropertyWithSetter(final Option<String> genericPropertyWithSetter){
            this.genericPropertyWithSetter = genericPropertyWithSetter == null ? Option.none() : genericPropertyWithSetter;
            return this;
        }

        public Builder inheritedGenericProperty(final Option<String> inheritedGenericProperty){
            this.inheritedGenericProperty = inheritedGenericProperty == null ? Option.none() : inheritedGenericProperty;
            return this;
        }

        public Builder inheritedNestedGenericProperty(final Option<Set<ListOperation<String>>> inheritedNestedGenericProperty){
            this.inheritedNestedGenericProperty = inheritedNestedGenericProperty == null ? Option.none() : inheritedNestedGenericProperty;
            return this;
        }

        public Builder overlappingGenericProperty(final Option<String> overlappingGenericProperty){
            this.overlappingGenericProperty = overlappingGenericProperty == null ? Option.none() : overlappingGenericProperty;
            return this;
        }

        public Builder overlappingNestedGenericProperty(final Option<Set<ListOperation<String>>> overlappingNestedGenericProperty){
            this.overlappingNestedGenericProperty = overlappingNestedGenericProperty == null ? Option.none() : overlappingNestedGenericProperty;
            return this;
        }

        public Builder inheritedGenericPropertyWithGetter(final Option<String> inheritedGenericPropertyWithGetter){
            this.inheritedGenericPropertyWithGetter = inheritedGenericPropertyWithGetter == null ? Option.none() : inheritedGenericPropertyWithGetter;
            return this;
        }

        public Builder inheritedGenericPropertyWithSetter(final Option<String> inheritedGenericPropertyWithSetter){
            this.inheritedGenericPropertyWithSetter = inheritedGenericPropertyWithSetter == null ? Option.none() : inheritedGenericPropertyWithSetter;
            return this;
        }


        public GenericChildClassWithParameterizedTypeDto build(){

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

        public static class Builder{

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

            public static  Builder newInstance(){
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

            public GenericChildClassWithParameterizedTypeDto.Projection build(){

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

    public static class DtoModel{

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