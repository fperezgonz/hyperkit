package solutions.sulfura.gend.dtos.generics.inheritance;

import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.projection.ProjectionFor;
import solutions.sulfura.gend.dtos.projection.DtoProjection;
import solutions.sulfura.gend.dtos.projection.fields.FieldConf.Presence;
import solutions.sulfura.gend.dtos.projection.fields.FieldConf;
import solutions.sulfura.gend.dtos.projection.fields.ListFieldConf;
import java.lang.String;
import io.vavr.control.Option;
import java.util.Set;
import solutions.sulfura.gend.dtos.ListOperation;
import solutions.sulfura.gend.dtos.generics.inheritance.GenericChildClassWithParameterizedType;

@DtoFor(GenericChildClassWithParameterizedType.class)
public class GenericChildClassWithParameterizedTypeDto implements Dto<GenericChildClassWithParameterizedType>{

    public Option<String> overlappingGenericProperty = Option.none();
    public Option<String> inheritedGenericPropertyWithGetter = Option.none();
    public Option<String> inheritedGenericProperty = Option.none();
    public Option<Set<ListOperation<String>>> inheritedNestedGenericProperty = Option.none();
    public Option<Set<ListOperation<String>>> overlappingNestedGenericProperty = Option.none();
    public Option<String> inheritedGenericPropertyWithSetter = Option.none();
    public Option<String> genericPropertyWithSetter = Option.none();
    public Option<String> genericProperty = Option.none();
    public Option<String> genericPropertyWithGetter = Option.none();
    public Option<Set<ListOperation<String>>> nestedGenericProperty = Option.none();

    public GenericChildClassWithParameterizedTypeDto(){}

    public Class<GenericChildClassWithParameterizedType> getSourceClass() {
        return GenericChildClassWithParameterizedType.class;
    }

    public static class Builder{

        public Option<String> overlappingGenericProperty = Option.none();
        public Option<String> inheritedGenericPropertyWithGetter = Option.none();
        public Option<String> inheritedGenericProperty = Option.none();
        public Option<Set<ListOperation<String>>> inheritedNestedGenericProperty = Option.none();
        public Option<Set<ListOperation<String>>> overlappingNestedGenericProperty = Option.none();
        public Option<String> inheritedGenericPropertyWithSetter = Option.none();
        public Option<String> genericPropertyWithSetter = Option.none();
        public Option<String> genericProperty = Option.none();
        public Option<String> genericPropertyWithGetter = Option.none();
        public Option<Set<ListOperation<String>>> nestedGenericProperty = Option.none();

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder overlappingGenericProperty(Option<String> overlappingGenericProperty){
            this.overlappingGenericProperty = overlappingGenericProperty == null ? Option.none() : overlappingGenericProperty;
            return this;
        }

        public Builder inheritedGenericPropertyWithGetter(Option<String> inheritedGenericPropertyWithGetter){
            this.inheritedGenericPropertyWithGetter = inheritedGenericPropertyWithGetter == null ? Option.none() : inheritedGenericPropertyWithGetter;
            return this;
        }

        public Builder inheritedGenericProperty(Option<String> inheritedGenericProperty){
            this.inheritedGenericProperty = inheritedGenericProperty == null ? Option.none() : inheritedGenericProperty;
            return this;
        }

        public Builder inheritedNestedGenericProperty(Option<Set<ListOperation<String>>> inheritedNestedGenericProperty){
            this.inheritedNestedGenericProperty = inheritedNestedGenericProperty == null ? Option.none() : inheritedNestedGenericProperty;
            return this;
        }

        public Builder overlappingNestedGenericProperty(Option<Set<ListOperation<String>>> overlappingNestedGenericProperty){
            this.overlappingNestedGenericProperty = overlappingNestedGenericProperty == null ? Option.none() : overlappingNestedGenericProperty;
            return this;
        }

        public Builder inheritedGenericPropertyWithSetter(Option<String> inheritedGenericPropertyWithSetter){
            this.inheritedGenericPropertyWithSetter = inheritedGenericPropertyWithSetter == null ? Option.none() : inheritedGenericPropertyWithSetter;
            return this;
        }

        public Builder genericPropertyWithSetter(Option<String> genericPropertyWithSetter){
            this.genericPropertyWithSetter = genericPropertyWithSetter == null ? Option.none() : genericPropertyWithSetter;
            return this;
        }

        public Builder genericProperty(Option<String> genericProperty){
            this.genericProperty = genericProperty == null ? Option.none() : genericProperty;
            return this;
        }

        public Builder genericPropertyWithGetter(Option<String> genericPropertyWithGetter){
            this.genericPropertyWithGetter = genericPropertyWithGetter == null ? Option.none() : genericPropertyWithGetter;
            return this;
        }

        public Builder nestedGenericProperty(Option<Set<ListOperation<String>>> nestedGenericProperty){
            this.nestedGenericProperty = nestedGenericProperty == null ? Option.none() : nestedGenericProperty;
            return this;
        }

        public GenericChildClassWithParameterizedTypeDto build(){
            GenericChildClassWithParameterizedTypeDto instance = new GenericChildClassWithParameterizedTypeDto();
            instance.overlappingGenericProperty = overlappingGenericProperty;
            instance.inheritedGenericPropertyWithGetter = inheritedGenericPropertyWithGetter;
            instance.inheritedGenericProperty = inheritedGenericProperty;
            instance.inheritedNestedGenericProperty = inheritedNestedGenericProperty;
            instance.overlappingNestedGenericProperty = overlappingNestedGenericProperty;
            instance.inheritedGenericPropertyWithSetter = inheritedGenericPropertyWithSetter;
            instance.genericPropertyWithSetter = genericPropertyWithSetter;
            instance.genericProperty = genericProperty;
            instance.genericPropertyWithGetter = genericPropertyWithGetter;
            instance.nestedGenericProperty = nestedGenericProperty;
            return instance;
        }

    }

    @ProjectionFor(GenericChildClassWithParameterizedTypeDto.class)
    public static class Projection extends DtoProjection<GenericChildClassWithParameterizedTypeDto>{

        public FieldConf overlappingGenericProperty;
        public FieldConf inheritedGenericPropertyWithGetter;
        public FieldConf inheritedGenericProperty;
        public ListFieldConf inheritedNestedGenericProperty;
        public ListFieldConf overlappingNestedGenericProperty;
        public FieldConf inheritedGenericPropertyWithSetter;
        public FieldConf genericPropertyWithSetter;
        public FieldConf genericProperty;
        public FieldConf genericPropertyWithGetter;
        public ListFieldConf nestedGenericProperty;

        public Projection(){}

        public static class Builder{

            public FieldConf overlappingGenericProperty;
            public FieldConf inheritedGenericPropertyWithGetter;
            public FieldConf inheritedGenericProperty;
            public ListFieldConf inheritedNestedGenericProperty;
            public ListFieldConf overlappingNestedGenericProperty;
            public FieldConf inheritedGenericPropertyWithSetter;
            public FieldConf genericPropertyWithSetter;
            public FieldConf genericProperty;
            public FieldConf genericPropertyWithGetter;
            public ListFieldConf nestedGenericProperty;

            public static  Builder newInstance(){
                return new Builder();
            }

            public Builder overlappingGenericProperty(FieldConf overlappingGenericProperty){
                this.overlappingGenericProperty = overlappingGenericProperty;
                return this;
            }

            public Builder overlappingGenericProperty(Presence presence){
                overlappingGenericProperty = FieldConf.of(presence);
                return this;
            }

            public Builder inheritedGenericPropertyWithGetter(FieldConf inheritedGenericPropertyWithGetter){
                this.inheritedGenericPropertyWithGetter = inheritedGenericPropertyWithGetter;
                return this;
            }

            public Builder inheritedGenericPropertyWithGetter(Presence presence){
                inheritedGenericPropertyWithGetter = FieldConf.of(presence);
                return this;
            }

            public Builder inheritedGenericProperty(FieldConf inheritedGenericProperty){
                this.inheritedGenericProperty = inheritedGenericProperty;
                return this;
            }

            public Builder inheritedGenericProperty(Presence presence){
                inheritedGenericProperty = FieldConf.of(presence);
                return this;
            }

            public Builder inheritedNestedGenericProperty(ListFieldConf inheritedNestedGenericProperty){
                this.inheritedNestedGenericProperty = inheritedNestedGenericProperty;
                return this;
            }

            public Builder inheritedNestedGenericProperty(Presence presence){
                inheritedNestedGenericProperty = ListFieldConf.of(presence);
                return this;
            }

            public Builder overlappingNestedGenericProperty(ListFieldConf overlappingNestedGenericProperty){
                this.overlappingNestedGenericProperty = overlappingNestedGenericProperty;
                return this;
            }

            public Builder overlappingNestedGenericProperty(Presence presence){
                overlappingNestedGenericProperty = ListFieldConf.of(presence);
                return this;
            }

            public Builder inheritedGenericPropertyWithSetter(FieldConf inheritedGenericPropertyWithSetter){
                this.inheritedGenericPropertyWithSetter = inheritedGenericPropertyWithSetter;
                return this;
            }

            public Builder inheritedGenericPropertyWithSetter(Presence presence){
                inheritedGenericPropertyWithSetter = FieldConf.of(presence);
                return this;
            }

            public Builder genericPropertyWithSetter(FieldConf genericPropertyWithSetter){
                this.genericPropertyWithSetter = genericPropertyWithSetter;
                return this;
            }

            public Builder genericPropertyWithSetter(Presence presence){
                genericPropertyWithSetter = FieldConf.of(presence);
                return this;
            }

            public Builder genericProperty(FieldConf genericProperty){
                this.genericProperty = genericProperty;
                return this;
            }

            public Builder genericProperty(Presence presence){
                genericProperty = FieldConf.of(presence);
                return this;
            }

            public Builder genericPropertyWithGetter(FieldConf genericPropertyWithGetter){
                this.genericPropertyWithGetter = genericPropertyWithGetter;
                return this;
            }

            public Builder genericPropertyWithGetter(Presence presence){
                genericPropertyWithGetter = FieldConf.of(presence);
                return this;
            }

            public Builder nestedGenericProperty(ListFieldConf nestedGenericProperty){
                this.nestedGenericProperty = nestedGenericProperty;
                return this;
            }

            public Builder nestedGenericProperty(Presence presence){
                nestedGenericProperty = ListFieldConf.of(presence);
                return this;
            }

            public GenericChildClassWithParameterizedTypeDto.Projection build(){
                GenericChildClassWithParameterizedTypeDto.Projection instance = new GenericChildClassWithParameterizedTypeDto.Projection();
                instance.overlappingGenericProperty = overlappingGenericProperty;
                instance.inheritedGenericPropertyWithGetter = inheritedGenericPropertyWithGetter;
                instance.inheritedGenericProperty = inheritedGenericProperty;
                instance.inheritedNestedGenericProperty = inheritedNestedGenericProperty;
                instance.overlappingNestedGenericProperty = overlappingNestedGenericProperty;
                instance.inheritedGenericPropertyWithSetter = inheritedGenericPropertyWithSetter;
                instance.genericPropertyWithSetter = genericPropertyWithSetter;
                instance.genericProperty = genericProperty;
                instance.genericPropertyWithGetter = genericPropertyWithGetter;
                instance.nestedGenericProperty = nestedGenericProperty;
                return instance;
            }

        }


    }

    public static class DtoModel{

        public static final String _overlappingGenericProperty = "overlappingGenericProperty";
        public static final String _inheritedGenericPropertyWithGetter = "inheritedGenericPropertyWithGetter";
        public static final String _inheritedGenericProperty = "inheritedGenericProperty";
        public static final String _inheritedNestedGenericProperty = "inheritedNestedGenericProperty";
        public static final String _overlappingNestedGenericProperty = "overlappingNestedGenericProperty";
        public static final String _inheritedGenericPropertyWithSetter = "inheritedGenericPropertyWithSetter";
        public static final String _genericPropertyWithSetter = "genericPropertyWithSetter";
        public static final String _genericProperty = "genericProperty";
        public static final String _genericPropertyWithGetter = "genericPropertyWithGetter";
        public static final String _nestedGenericProperty = "nestedGenericProperty";

    }

}