package solutions.sulfura.gend.dtos.generics.inheritance;

import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.conf.DtoConf;
import solutions.sulfura.gend.dtos.conf.fields.FieldConf;
import solutions.sulfura.gend.dtos.conf.fields.ListFieldConf;
import java.lang.String;
import io.vavr.control.Option;
import java.util.Set;
import solutions.sulfura.gend.dtos.ListOperation;
import solutions.sulfura.gend.dtos.generics.inheritance.GenericChildClassWithParameterizedType;

@DtoFor(GenericChildClassWithParameterizedType.class)
public class GenericChildClassWithParameterizedTypeDto implements Dto<GenericChildClassWithParameterizedType>{

    public Option<String> overlappingGenericProperty = Option.some(null);
    public Option<String> inheritedGenericPropertyWithGetter = Option.some(null);
    public Option<String> inheritedGenericProperty = Option.some(null);
    public Option<Set<ListOperation<String>>> inheritedNestedGenericProperty = Option.some(null);
    public Option<Set<ListOperation<String>>> overlappingNestedGenericProperty = Option.some(null);
    public Option<String> inheritedGenericPropertyWithSetter = Option.some(null);
    public Option<String> genericPropertyWithSetter = Option.some(null);
    public Option<String> genericProperty = Option.some(null);
    public Option<String> genericPropertyWithGetter = Option.some(null);
    public Option<Set<ListOperation<String>>> nestedGenericProperty = Option.some(null);

    public GenericChildClassWithParameterizedTypeDto(){}

    public static class Builder{

        public Option<String> overlappingGenericProperty = Option.some(null);
        public Option<String> inheritedGenericPropertyWithGetter = Option.some(null);
        public Option<String> inheritedGenericProperty = Option.some(null);
        public Option<Set<ListOperation<String>>> inheritedNestedGenericProperty = Option.some(null);
        public Option<Set<ListOperation<String>>> overlappingNestedGenericProperty = Option.some(null);
        public Option<String> inheritedGenericPropertyWithSetter = Option.some(null);
        public Option<String> genericPropertyWithSetter = Option.some(null);
        public Option<String> genericProperty = Option.some(null);
        public Option<String> genericPropertyWithGetter = Option.some(null);
        public Option<Set<ListOperation<String>>> nestedGenericProperty = Option.some(null);

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder overlappingGenericProperty(Option<String> overlappingGenericProperty){
            this.overlappingGenericProperty = overlappingGenericProperty == null ? Option.some(null) : overlappingGenericProperty;
            return this;
        }

        public Builder inheritedGenericPropertyWithGetter(Option<String> inheritedGenericPropertyWithGetter){
            this.inheritedGenericPropertyWithGetter = inheritedGenericPropertyWithGetter == null ? Option.some(null) : inheritedGenericPropertyWithGetter;
            return this;
        }

        public Builder inheritedGenericProperty(Option<String> inheritedGenericProperty){
            this.inheritedGenericProperty = inheritedGenericProperty == null ? Option.some(null) : inheritedGenericProperty;
            return this;
        }

        public Builder inheritedNestedGenericProperty(Option<Set<ListOperation<String>>> inheritedNestedGenericProperty){
            this.inheritedNestedGenericProperty = inheritedNestedGenericProperty == null ? Option.some(null) : inheritedNestedGenericProperty;
            return this;
        }

        public Builder overlappingNestedGenericProperty(Option<Set<ListOperation<String>>> overlappingNestedGenericProperty){
            this.overlappingNestedGenericProperty = overlappingNestedGenericProperty == null ? Option.some(null) : overlappingNestedGenericProperty;
            return this;
        }

        public Builder inheritedGenericPropertyWithSetter(Option<String> inheritedGenericPropertyWithSetter){
            this.inheritedGenericPropertyWithSetter = inheritedGenericPropertyWithSetter == null ? Option.some(null) : inheritedGenericPropertyWithSetter;
            return this;
        }

        public Builder genericPropertyWithSetter(Option<String> genericPropertyWithSetter){
            this.genericPropertyWithSetter = genericPropertyWithSetter == null ? Option.some(null) : genericPropertyWithSetter;
            return this;
        }

        public Builder genericProperty(Option<String> genericProperty){
            this.genericProperty = genericProperty == null ? Option.some(null) : genericProperty;
            return this;
        }

        public Builder genericPropertyWithGetter(Option<String> genericPropertyWithGetter){
            this.genericPropertyWithGetter = genericPropertyWithGetter == null ? Option.some(null) : genericPropertyWithGetter;
            return this;
        }

        public Builder nestedGenericProperty(Option<Set<ListOperation<String>>> nestedGenericProperty){
            this.nestedGenericProperty = nestedGenericProperty == null ? Option.some(null) : nestedGenericProperty;
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

    public static class Conf extends DtoConf<GenericChildClassWithParameterizedTypeDto>{

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

        public Conf(){}

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

            public Builder inheritedGenericPropertyWithGetter(FieldConf inheritedGenericPropertyWithGetter){
                this.inheritedGenericPropertyWithGetter = inheritedGenericPropertyWithGetter;
                return this;
            }

            public Builder inheritedGenericProperty(FieldConf inheritedGenericProperty){
                this.inheritedGenericProperty = inheritedGenericProperty;
                return this;
            }

            public Builder inheritedNestedGenericProperty(ListFieldConf inheritedNestedGenericProperty){
                this.inheritedNestedGenericProperty = inheritedNestedGenericProperty;
                return this;
            }

            public Builder overlappingNestedGenericProperty(ListFieldConf overlappingNestedGenericProperty){
                this.overlappingNestedGenericProperty = overlappingNestedGenericProperty;
                return this;
            }

            public Builder inheritedGenericPropertyWithSetter(FieldConf inheritedGenericPropertyWithSetter){
                this.inheritedGenericPropertyWithSetter = inheritedGenericPropertyWithSetter;
                return this;
            }

            public Builder genericPropertyWithSetter(FieldConf genericPropertyWithSetter){
                this.genericPropertyWithSetter = genericPropertyWithSetter;
                return this;
            }

            public Builder genericProperty(FieldConf genericProperty){
                this.genericProperty = genericProperty;
                return this;
            }

            public Builder genericPropertyWithGetter(FieldConf genericPropertyWithGetter){
                this.genericPropertyWithGetter = genericPropertyWithGetter;
                return this;
            }

            public Builder nestedGenericProperty(ListFieldConf nestedGenericProperty){
                this.nestedGenericProperty = nestedGenericProperty;
                return this;
            }

            public GenericChildClassWithParameterizedTypeDto.Conf build(){
                GenericChildClassWithParameterizedTypeDto.Conf instance = new GenericChildClassWithParameterizedTypeDto.Conf();
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

}