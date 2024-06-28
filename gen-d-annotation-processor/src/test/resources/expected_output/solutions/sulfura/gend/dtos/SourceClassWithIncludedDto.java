package solutions.sulfura.gend.dtos;

import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.conf.DtoConf;
import solutions.sulfura.gend.dtos.conf.fields.FieldConf;
import java.lang.String;
import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.SourceClassWithIncluded;

@DtoFor(SourceClassWithIncluded.class)
public class SourceClassWithIncludedDto implements Dto<SourceClassWithIncluded>{

    public Option<String> stringPropertyWithGetter = Option.some(null);
    public Option<String> stringPropertyWithCustomAnnotation = Option.some(null);
    public Option<String> stringPropertyWithSetter = Option.some(null);
    public Option<String> stringProperty = Option.some(null);
    public Option<String> stringPropertyWithSetterAndCustomAnnotation = Option.some(null);

    public SourceClassWithIncludedDto(){}

    public Class<SourceClassWithIncluded> getSourceClass() {
        return SourceClassWithIncluded.class;
    }

    public static class Builder{

        public Option<String> stringPropertyWithGetter = Option.some(null);
        public Option<String> stringPropertyWithCustomAnnotation = Option.some(null);
        public Option<String> stringPropertyWithSetter = Option.some(null);
        public Option<String> stringProperty = Option.some(null);
        public Option<String> stringPropertyWithSetterAndCustomAnnotation = Option.some(null);

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder stringPropertyWithGetter(Option<String> stringPropertyWithGetter){
            this.stringPropertyWithGetter = stringPropertyWithGetter == null ? Option.some(null) : stringPropertyWithGetter;
            return this;
        }

        public Builder stringPropertyWithCustomAnnotation(Option<String> stringPropertyWithCustomAnnotation){
            this.stringPropertyWithCustomAnnotation = stringPropertyWithCustomAnnotation == null ? Option.some(null) : stringPropertyWithCustomAnnotation;
            return this;
        }

        public Builder stringPropertyWithSetter(Option<String> stringPropertyWithSetter){
            this.stringPropertyWithSetter = stringPropertyWithSetter == null ? Option.some(null) : stringPropertyWithSetter;
            return this;
        }

        public Builder stringProperty(Option<String> stringProperty){
            this.stringProperty = stringProperty == null ? Option.some(null) : stringProperty;
            return this;
        }

        public Builder stringPropertyWithSetterAndCustomAnnotation(Option<String> stringPropertyWithSetterAndCustomAnnotation){
            this.stringPropertyWithSetterAndCustomAnnotation = stringPropertyWithSetterAndCustomAnnotation == null ? Option.some(null) : stringPropertyWithSetterAndCustomAnnotation;
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

    public static class Conf extends DtoConf<SourceClassWithIncludedDto>{

        public FieldConf stringPropertyWithGetter;
        public FieldConf stringPropertyWithCustomAnnotation;
        public FieldConf stringPropertyWithSetter;
        public FieldConf stringProperty;
        public FieldConf stringPropertyWithSetterAndCustomAnnotation;

        public Conf(){}

        public static class Builder{

            public FieldConf stringPropertyWithGetter;
            public FieldConf stringPropertyWithCustomAnnotation;
            public FieldConf stringPropertyWithSetter;
            public FieldConf stringProperty;
            public FieldConf stringPropertyWithSetterAndCustomAnnotation;

            public static  Builder newInstance(){
                return new Builder();
            }

            public Builder stringPropertyWithGetter(FieldConf stringPropertyWithGetter){
                this.stringPropertyWithGetter = stringPropertyWithGetter;
                return this;
            }

            public Builder stringPropertyWithCustomAnnotation(FieldConf stringPropertyWithCustomAnnotation){
                this.stringPropertyWithCustomAnnotation = stringPropertyWithCustomAnnotation;
                return this;
            }

            public Builder stringPropertyWithSetter(FieldConf stringPropertyWithSetter){
                this.stringPropertyWithSetter = stringPropertyWithSetter;
                return this;
            }

            public Builder stringProperty(FieldConf stringProperty){
                this.stringProperty = stringProperty;
                return this;
            }

            public Builder stringPropertyWithSetterAndCustomAnnotation(FieldConf stringPropertyWithSetterAndCustomAnnotation){
                this.stringPropertyWithSetterAndCustomAnnotation = stringPropertyWithSetterAndCustomAnnotation;
                return this;
            }

            public SourceClassWithIncludedDto.Conf build(){
                SourceClassWithIncludedDto.Conf instance = new SourceClassWithIncludedDto.Conf();
                instance.stringPropertyWithGetter = stringPropertyWithGetter;
                instance.stringPropertyWithCustomAnnotation = stringPropertyWithCustomAnnotation;
                instance.stringPropertyWithSetter = stringPropertyWithSetter;
                instance.stringProperty = stringProperty;
                instance.stringPropertyWithSetterAndCustomAnnotation = stringPropertyWithSetterAndCustomAnnotation;
                return instance;
            }

        }


    }

}