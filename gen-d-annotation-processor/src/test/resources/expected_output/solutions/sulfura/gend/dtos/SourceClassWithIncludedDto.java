package solutions.sulfura.gend.dtos;

import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.projection.ProjectionFor;
import solutions.sulfura.gend.dtos.projection.DtoProjection;
import solutions.sulfura.gend.dtos.projection.fields.FieldConf.Presence;
import solutions.sulfura.gend.dtos.projection.fields.FieldConf;
import java.lang.String;
import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.SourceClassWithIncluded;

@DtoFor(SourceClassWithIncluded.class)
public class SourceClassWithIncludedDto implements Dto<SourceClassWithIncluded>{

    public Option<String> stringPropertyWithGetter = Option.none();
    public Option<String> stringPropertyWithCustomAnnotation = Option.none();
    public Option<String> stringPropertyWithSetter = Option.none();
    public Option<String> stringProperty = Option.none();
    public Option<String> stringPropertyWithSetterAndCustomAnnotation = Option.none();

    public SourceClassWithIncludedDto(){}

    public Class<SourceClassWithIncluded> getSourceClass() {
        return SourceClassWithIncluded.class;
    }

    public static class Builder{

        public Option<String> stringPropertyWithGetter = Option.none();
        public Option<String> stringPropertyWithCustomAnnotation = Option.none();
        public Option<String> stringPropertyWithSetter = Option.none();
        public Option<String> stringProperty = Option.none();
        public Option<String> stringPropertyWithSetterAndCustomAnnotation = Option.none();

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder stringPropertyWithGetter(Option<String> stringPropertyWithGetter){
            this.stringPropertyWithGetter = stringPropertyWithGetter == null ? Option.none() : stringPropertyWithGetter;
            return this;
        }

        public Builder stringPropertyWithCustomAnnotation(Option<String> stringPropertyWithCustomAnnotation){
            this.stringPropertyWithCustomAnnotation = stringPropertyWithCustomAnnotation == null ? Option.none() : stringPropertyWithCustomAnnotation;
            return this;
        }

        public Builder stringPropertyWithSetter(Option<String> stringPropertyWithSetter){
            this.stringPropertyWithSetter = stringPropertyWithSetter == null ? Option.none() : stringPropertyWithSetter;
            return this;
        }

        public Builder stringProperty(Option<String> stringProperty){
            this.stringProperty = stringProperty == null ? Option.none() : stringProperty;
            return this;
        }

        public Builder stringPropertyWithSetterAndCustomAnnotation(Option<String> stringPropertyWithSetterAndCustomAnnotation){
            this.stringPropertyWithSetterAndCustomAnnotation = stringPropertyWithSetterAndCustomAnnotation == null ? Option.none() : stringPropertyWithSetterAndCustomAnnotation;
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

    @ProjectionFor(SourceClassWithIncludedDto.class)
    public static class Projection extends DtoProjection<SourceClassWithIncludedDto>{

        public FieldConf stringPropertyWithGetter;
        public FieldConf stringPropertyWithCustomAnnotation;
        public FieldConf stringPropertyWithSetter;
        public FieldConf stringProperty;
        public FieldConf stringPropertyWithSetterAndCustomAnnotation;

        public Projection(){}

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

            public Builder stringPropertyWithGetter(Presence presence){
                stringPropertyWithGetter = FieldConf.of(presence);
                return this;
            }

            public Builder stringPropertyWithCustomAnnotation(FieldConf stringPropertyWithCustomAnnotation){
                this.stringPropertyWithCustomAnnotation = stringPropertyWithCustomAnnotation;
                return this;
            }

            public Builder stringPropertyWithCustomAnnotation(Presence presence){
                stringPropertyWithCustomAnnotation = FieldConf.of(presence);
                return this;
            }

            public Builder stringPropertyWithSetter(FieldConf stringPropertyWithSetter){
                this.stringPropertyWithSetter = stringPropertyWithSetter;
                return this;
            }

            public Builder stringPropertyWithSetter(Presence presence){
                stringPropertyWithSetter = FieldConf.of(presence);
                return this;
            }

            public Builder stringProperty(FieldConf stringProperty){
                this.stringProperty = stringProperty;
                return this;
            }

            public Builder stringProperty(Presence presence){
                stringProperty = FieldConf.of(presence);
                return this;
            }

            public Builder stringPropertyWithSetterAndCustomAnnotation(FieldConf stringPropertyWithSetterAndCustomAnnotation){
                this.stringPropertyWithSetterAndCustomAnnotation = stringPropertyWithSetterAndCustomAnnotation;
                return this;
            }

            public Builder stringPropertyWithSetterAndCustomAnnotation(Presence presence){
                stringPropertyWithSetterAndCustomAnnotation = FieldConf.of(presence);
                return this;
            }

            public SourceClassWithIncludedDto.Projection build(){
                SourceClassWithIncludedDto.Projection instance = new SourceClassWithIncludedDto.Projection();
                instance.stringPropertyWithGetter = stringPropertyWithGetter;
                instance.stringPropertyWithCustomAnnotation = stringPropertyWithCustomAnnotation;
                instance.stringPropertyWithSetter = stringPropertyWithSetter;
                instance.stringProperty = stringProperty;
                instance.stringPropertyWithSetterAndCustomAnnotation = stringPropertyWithSetterAndCustomAnnotation;
                return instance;
            }

        }


    }

    public static class DtoModel{

        public static final String _stringPropertyWithGetter = "stringPropertyWithGetter";
        public static final String _stringPropertyWithCustomAnnotation = "stringPropertyWithCustomAnnotation";
        public static final String _stringPropertyWithSetter = "stringPropertyWithSetter";
        public static final String _stringProperty = "stringProperty";
        public static final String _stringPropertyWithSetterAndCustomAnnotation = "stringPropertyWithSetterAndCustomAnnotation";

    }

}