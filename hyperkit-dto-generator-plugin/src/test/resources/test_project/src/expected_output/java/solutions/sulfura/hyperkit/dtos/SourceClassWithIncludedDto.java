package solutions.sulfura.hyperkit.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.SourceClassWithIncluded;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;

@DtoFor(SourceClassWithIncluded.class)
public class SourceClassWithIncludedDto implements Dto<SourceClassWithIncluded> {

    public ValueWrapper<String> stringProperty = ValueWrapper.empty();
    public ValueWrapper<String> stringPropertyWithCustomAnnotation = ValueWrapper.empty();
    public ValueWrapper<String> stringPropertyWithGetter = ValueWrapper.empty();
    public ValueWrapper<String> stringPropertyWithSetter = ValueWrapper.empty();
    public ValueWrapper<String> stringPropertyWithSetterAndCustomAnnotation = ValueWrapper.empty();

    public SourceClassWithIncludedDto() {
    }

    public Class<SourceClassWithIncluded> getSourceClass() {
        return SourceClassWithIncluded.class;
    }

    public static class Builder {

        ValueWrapper<String> stringProperty = ValueWrapper.empty();
        ValueWrapper<String> stringPropertyWithCustomAnnotation = ValueWrapper.empty();
        ValueWrapper<String> stringPropertyWithGetter = ValueWrapper.empty();
        ValueWrapper<String> stringPropertyWithSetter = ValueWrapper.empty();
        ValueWrapper<String> stringPropertyWithSetterAndCustomAnnotation = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder stringProperty(final ValueWrapper<String> stringProperty){
            this.stringProperty = stringProperty == null ? ValueWrapper.empty() : stringProperty;
            return this;
        }

        public Builder stringPropertyWithCustomAnnotation(final ValueWrapper<String> stringPropertyWithCustomAnnotation){
            this.stringPropertyWithCustomAnnotation = stringPropertyWithCustomAnnotation == null ? ValueWrapper.empty() : stringPropertyWithCustomAnnotation;
            return this;
        }

        public Builder stringPropertyWithGetter(final ValueWrapper<String> stringPropertyWithGetter){
            this.stringPropertyWithGetter = stringPropertyWithGetter == null ? ValueWrapper.empty() : stringPropertyWithGetter;
            return this;
        }

        public Builder stringPropertyWithSetter(final ValueWrapper<String> stringPropertyWithSetter){
            this.stringPropertyWithSetter = stringPropertyWithSetter == null ? ValueWrapper.empty() : stringPropertyWithSetter;
            return this;
        }

        public Builder stringPropertyWithSetterAndCustomAnnotation(final ValueWrapper<String> stringPropertyWithSetterAndCustomAnnotation){
            this.stringPropertyWithSetterAndCustomAnnotation = stringPropertyWithSetterAndCustomAnnotation == null ? ValueWrapper.empty() : stringPropertyWithSetterAndCustomAnnotation;
            return this;
        }


        public SourceClassWithIncludedDto build() {

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

        public static class Builder {

            FieldConf stringProperty;
            FieldConf stringPropertyWithCustomAnnotation;
            FieldConf stringPropertyWithGetter;
            FieldConf stringPropertyWithSetter;
            FieldConf stringPropertyWithSetterAndCustomAnnotation;

            public static Builder newInstance() {
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

            public SourceClassWithIncludedDto.Projection build() {

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

    public static class DtoModel {

        public static final String _stringProperty = "stringProperty";
        public static final String _stringPropertyWithCustomAnnotation = "stringPropertyWithCustomAnnotation";
        public static final String _stringPropertyWithGetter = "stringPropertyWithGetter";
        public static final String _stringPropertyWithSetter = "stringPropertyWithSetter";
        public static final String _stringPropertyWithSetterAndCustomAnnotation = "stringPropertyWithSetterAndCustomAnnotation";

    }

}