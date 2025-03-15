package solutions.sulfura.gend.dtos;

import solutions.sulfura.gend.dtos.SourceClassWithIncludedDto.Builder;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.SourceClassWithIncludedDto;
import solutions.sulfura.gend.dtos.projection.fields.FieldConf;
import solutions.sulfura.gend.dtos.projection.DtoProjectionException;
import solutions.sulfura.gend.dtos.projection.DtoProjection;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.SourceClassWithIncludedDto.Projection;
import solutions.sulfura.gend.dtos.SourceClassWithIncludedDto.DtoModel;
import solutions.sulfura.gend.dtos.SourceClassWithIncluded;
import solutions.sulfura.gend.dtos.projection.ProjectionFor;
import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.projection.ProjectionUtils;
import solutions.sulfura.gend.dtos.projection.fields.FieldConf.Presence;

@DtoFor(SourceClassWithIncluded.class)
public class SourceClassWithIncludedDto implements Dto<SourceClassWithIncluded>{

    public Option<String> stringProperty = Option.none();
    public Option<String> stringPropertyWithCustomAnnotation = Option.none();
    public Option<String> stringPropertyWithGetter = Option.none();
    public Option<String> stringPropertyWithSetter = Option.none();
    public Option<String> stringPropertyWithSetterAndCustomAnnotation = Option.none();

    public SourceClassWithIncludedDto() {
    }

    public Class<SourceClassWithIncluded> getSourceClass() {
        return SourceClassWithIncluded.class;
    }

    public static class Builder{

        Option<String> stringProperty = Option.none();
        Option<String> stringPropertyWithCustomAnnotation = Option.none();
        Option<String> stringPropertyWithGetter = Option.none();
        Option<String> stringPropertyWithSetter = Option.none();
        Option<String> stringPropertyWithSetterAndCustomAnnotation = Option.none();

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder stringProperty(final Option<String> stringProperty){
            this.stringProperty = stringProperty == null ? Option.none() : stringProperty;
            return this;
        }

        public Builder stringPropertyWithCustomAnnotation(final Option<String> stringPropertyWithCustomAnnotation){
            this.stringPropertyWithCustomAnnotation = stringPropertyWithCustomAnnotation == null ? Option.none() : stringPropertyWithCustomAnnotation;
            return this;
        }

        public Builder stringPropertyWithGetter(final Option<String> stringPropertyWithGetter){
            this.stringPropertyWithGetter = stringPropertyWithGetter == null ? Option.none() : stringPropertyWithGetter;
            return this;
        }

        public Builder stringPropertyWithSetter(final Option<String> stringPropertyWithSetter){
            this.stringPropertyWithSetter = stringPropertyWithSetter == null ? Option.none() : stringPropertyWithSetter;
            return this;
        }

        public Builder stringPropertyWithSetterAndCustomAnnotation(final Option<String> stringPropertyWithSetterAndCustomAnnotation){
            this.stringPropertyWithSetterAndCustomAnnotation = stringPropertyWithSetterAndCustomAnnotation == null ? Option.none() : stringPropertyWithSetterAndCustomAnnotation;
            return this;
        }


        public SourceClassWithIncludedDto build(){

            SourceClassWithIncludedDto instance = new SourceClassWithIncludedDto();
            instance.stringProperty = stringProperty;
            instance.stringPropertyWithCustomAnnotation = stringPropertyWithCustomAnnotation;
            instance.stringPropertyWithGetter = stringPropertyWithGetter;
            instance.stringPropertyWithSetter = stringPropertyWithSetter;
            instance.stringPropertyWithSetterAndCustomAnnotation = stringPropertyWithSetterAndCustomAnnotation;

            return instance;

        }

    }

    @ProjectionFor(SourceClassWithIncludedDto.class)
    public static class Projection extends DtoProjection<SourceClassWithIncludedDto> {

        public FieldConf stringProperty;
        public FieldConf stringPropertyWithCustomAnnotation;
        public FieldConf stringPropertyWithGetter;
        public FieldConf stringPropertyWithSetter;
        public FieldConf stringPropertyWithSetterAndCustomAnnotation;

        public Projection() {
        }

        public void applyProjectionTo(SourceClassWithIncludedDto dto) throws DtoProjectionException {
            dto.stringProperty = ProjectionUtils.getProjectedValue(dto.stringProperty, this.stringProperty);
            dto.stringPropertyWithCustomAnnotation = ProjectionUtils.getProjectedValue(dto.stringPropertyWithCustomAnnotation, this.stringPropertyWithCustomAnnotation);
            dto.stringPropertyWithGetter = ProjectionUtils.getProjectedValue(dto.stringPropertyWithGetter, this.stringPropertyWithGetter);
            dto.stringPropertyWithSetter = ProjectionUtils.getProjectedValue(dto.stringPropertyWithSetter, this.stringPropertyWithSetter);
            dto.stringPropertyWithSetterAndCustomAnnotation = ProjectionUtils.getProjectedValue(dto.stringPropertyWithSetterAndCustomAnnotation, this.stringPropertyWithSetterAndCustomAnnotation);
        }

        public static class Builder{

            FieldConf stringProperty;
            FieldConf stringPropertyWithCustomAnnotation;
            FieldConf stringPropertyWithGetter;
            FieldConf stringPropertyWithSetter;
            FieldConf stringPropertyWithSetterAndCustomAnnotation;

            public static  Builder newInstance(){
                return new Builder();
            }

            public Builder stringProperty(final FieldConf stringProperty){
                this.stringProperty = stringProperty;
                return this;
            }

            public Builder stringProperty(final Presence presence){
                stringProperty = FieldConf.of(presence);
                return this;
            }

            public Builder stringPropertyWithCustomAnnotation(final FieldConf stringPropertyWithCustomAnnotation){
                this.stringPropertyWithCustomAnnotation = stringPropertyWithCustomAnnotation;
                return this;
            }

            public Builder stringPropertyWithCustomAnnotation(final Presence presence){
                stringPropertyWithCustomAnnotation = FieldConf.of(presence);
                return this;
            }

            public Builder stringPropertyWithGetter(final FieldConf stringPropertyWithGetter){
                this.stringPropertyWithGetter = stringPropertyWithGetter;
                return this;
            }

            public Builder stringPropertyWithGetter(final Presence presence){
                stringPropertyWithGetter = FieldConf.of(presence);
                return this;
            }

            public Builder stringPropertyWithSetter(final FieldConf stringPropertyWithSetter){
                this.stringPropertyWithSetter = stringPropertyWithSetter;
                return this;
            }

            public Builder stringPropertyWithSetter(final Presence presence){
                stringPropertyWithSetter = FieldConf.of(presence);
                return this;
            }

            public Builder stringPropertyWithSetterAndCustomAnnotation(final FieldConf stringPropertyWithSetterAndCustomAnnotation){
                this.stringPropertyWithSetterAndCustomAnnotation = stringPropertyWithSetterAndCustomAnnotation;
                return this;
            }

            public Builder stringPropertyWithSetterAndCustomAnnotation(final Presence presence){
                stringPropertyWithSetterAndCustomAnnotation = FieldConf.of(presence);
                return this;
            }

            public SourceClassWithIncludedDto.Projection build(){

                SourceClassWithIncludedDto.Projection instance = new SourceClassWithIncludedDto.Projection();
                instance.stringProperty = stringProperty;
                instance.stringPropertyWithCustomAnnotation = stringPropertyWithCustomAnnotation;
                instance.stringPropertyWithGetter = stringPropertyWithGetter;
                instance.stringPropertyWithSetter = stringPropertyWithSetter;
                instance.stringPropertyWithSetterAndCustomAnnotation = stringPropertyWithSetterAndCustomAnnotation;

                return instance;

            }

        }

    }

    public static class DtoModel{

        public static final String _stringProperty = "stringProperty";
        public static final String _stringPropertyWithCustomAnnotation = "stringPropertyWithCustomAnnotation";
        public static final String _stringPropertyWithGetter = "stringPropertyWithGetter";
        public static final String _stringPropertyWithSetter = "stringPropertyWithSetter";
        public static final String _stringPropertyWithSetterAndCustomAnnotation = "stringPropertyWithSetterAndCustomAnnotation";

    }

}