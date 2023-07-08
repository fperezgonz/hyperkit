package solutions.sulfura.gend.dtos;

import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.SourceClassWithIncluded;
import java.lang.String;

@DtoFor(SourceClassWithIncluded.class)
public class SourceClassWithIncludedDto implements Dto<SourceClassWithIncluded>{

    public Option<String> stringPropertyWithGetter;
    public Option<String> stringPropertyWithCustomAnnotation;
    public Option<String> stringPropertyWithSetter;
    public Option<String> stringProperty;
    public Option<String> stringPropertyWithSetterAndCustomAnnotation;

    public SourceClassWithIncludedDto(){}

    public static  Builder builder(){
        return new Builder();
    }

    public static class Builder{

        public Option<String> stringPropertyWithGetter;
        public Option<String> stringPropertyWithCustomAnnotation;
        public Option<String> stringPropertyWithSetter;
        public Option<String> stringProperty;
        public Option<String> stringPropertyWithSetterAndCustomAnnotation;

        public Builder stringPropertyWithGetter(Option<String> stringPropertyWithGetter){
            this.stringPropertyWithGetter = stringPropertyWithGetter;
            return this;
        }

        public Builder stringPropertyWithCustomAnnotation(Option<String> stringPropertyWithCustomAnnotation){
            this.stringPropertyWithCustomAnnotation = stringPropertyWithCustomAnnotation;
            return this;
        }

        public Builder stringPropertyWithSetter(Option<String> stringPropertyWithSetter){
            this.stringPropertyWithSetter = stringPropertyWithSetter;
            return this;
        }

        public Builder stringProperty(Option<String> stringProperty){
            this.stringProperty = stringProperty;
            return this;
        }

        public Builder stringPropertyWithSetterAndCustomAnnotation(Option<String> stringPropertyWithSetterAndCustomAnnotation){
            this.stringPropertyWithSetterAndCustomAnnotation = stringPropertyWithSetterAndCustomAnnotation;
            return this;
        }

        public SourceClassWithIncludedDto build(){
            SourceClassWithIncludedDto instance = new SourceClassWithIncludedDto();
            instance.stringPropertyWithGetter = stringPropertyWithGetter;
            instance.stringPropertyWithCustomAnnotation = stringPropertyWithCustomAnnotation;
            instance.stringPropertyWithSetter = stringPropertyWithSetter;
            instance.stringProperty = stringProperty;
            instance.stringPropertyWithSetterAndCustomAnnotation = stringPropertyWithSetterAndCustomAnnotation;
            return instance;
        }

    }

}