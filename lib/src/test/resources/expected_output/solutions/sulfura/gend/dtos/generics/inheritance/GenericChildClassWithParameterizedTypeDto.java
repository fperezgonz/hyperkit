package solutions.sulfura.gend.dtos.generics.inheritance;

import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.generics.inheritance.GenericChildClassWithParameterizedType;
import java.lang.String;
import java.util.Set;

@DtoFor(GenericChildClassWithParameterizedType.class)
public class GenericChildClassWithParameterizedTypeDto implements Dto<GenericChildClassWithParameterizedType>{

    public Option<String> overlappingGenericProperty;
    public Option<String> inheritedGenericPropertyWithGetter;
    public Option<String> inheritedGenericProperty;
    public Option<Set<String>> inheritedNestedGenericProperty;
    public Option<Set<String>> overlappingNestedGenericProperty;
    public Option<String> inheritedGenericPropertyWithSetter;
    public Option<String> genericPropertyWithSetter;
    public Option<String> genericProperty;
    public Option<String> genericPropertyWithGetter;
    public Option<Set<String>> nestedGenericProperty;

    public GenericChildClassWithParameterizedTypeDto(){}

    public static  Builder builder(){
        return new Builder();
    }

    public static class Builder{

        public Option<String> overlappingGenericProperty;
        public Option<String> inheritedGenericPropertyWithGetter;
        public Option<String> inheritedGenericProperty;
        public Option<Set<String>> inheritedNestedGenericProperty;
        public Option<Set<String>> overlappingNestedGenericProperty;
        public Option<String> inheritedGenericPropertyWithSetter;
        public Option<String> genericPropertyWithSetter;
        public Option<String> genericProperty;
        public Option<String> genericPropertyWithGetter;
        public Option<Set<String>> nestedGenericProperty;

        public Builder overlappingGenericProperty(Option<String> overlappingGenericProperty){
            this.overlappingGenericProperty = overlappingGenericProperty;
            return this;
        }

        public Builder inheritedGenericPropertyWithGetter(Option<String> inheritedGenericPropertyWithGetter){
            this.inheritedGenericPropertyWithGetter = inheritedGenericPropertyWithGetter;
            return this;
        }

        public Builder inheritedGenericProperty(Option<String> inheritedGenericProperty){
            this.inheritedGenericProperty = inheritedGenericProperty;
            return this;
        }

        public Builder inheritedNestedGenericProperty(Option<Set<String>> inheritedNestedGenericProperty){
            this.inheritedNestedGenericProperty = inheritedNestedGenericProperty;
            return this;
        }

        public Builder overlappingNestedGenericProperty(Option<Set<String>> overlappingNestedGenericProperty){
            this.overlappingNestedGenericProperty = overlappingNestedGenericProperty;
            return this;
        }

        public Builder inheritedGenericPropertyWithSetter(Option<String> inheritedGenericPropertyWithSetter){
            this.inheritedGenericPropertyWithSetter = inheritedGenericPropertyWithSetter;
            return this;
        }

        public Builder genericPropertyWithSetter(Option<String> genericPropertyWithSetter){
            this.genericPropertyWithSetter = genericPropertyWithSetter;
            return this;
        }

        public Builder genericProperty(Option<String> genericProperty){
            this.genericProperty = genericProperty;
            return this;
        }

        public Builder genericPropertyWithGetter(Option<String> genericPropertyWithGetter){
            this.genericPropertyWithGetter = genericPropertyWithGetter;
            return this;
        }

        public Builder nestedGenericProperty(Option<Set<String>> nestedGenericProperty){
            this.nestedGenericProperty = nestedGenericProperty;
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

}