package solutions.sulfura.hyperkit.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.SourceClassBooleanGetterSetter;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;

@DtoFor(SourceClassBooleanGetterSetter.class)
public class SourceClassBooleanGetterSetterDto implements Dto<SourceClassBooleanGetterSetter> {

    public ValueWrapper<Boolean> booleanPropertyWithGetter = ValueWrapper.empty();
    public ValueWrapper<Boolean> booleanPropertyWithGetterAndSetter = ValueWrapper.empty();
    public ValueWrapper<Boolean> boxedBooleanPropertyWithGetter = ValueWrapper.empty();
    public ValueWrapper<Boolean> boxedBooleanPropertyWithGetterAndSetter = ValueWrapper.empty();

    public SourceClassBooleanGetterSetterDto() {
    }

    public Class<SourceClassBooleanGetterSetter> getSourceClass() {
        return SourceClassBooleanGetterSetter.class;
    }

    public ValueWrapper<Boolean> isBooleanPropertyWithGetter() {
        return this.booleanPropertyWithGetter;
    }

    public ValueWrapper<Boolean> isBoxedBooleanPropertyWithGetter() {
        return this.boxedBooleanPropertyWithGetter;
    }

    public void setBooleanPropertyWithGetterAndSetter(ValueWrapper<Boolean> value) {
        this.booleanPropertyWithGetterAndSetter = value;
    }

    public void setBoxedBooleanPropertyWithGetterAndSetter(ValueWrapper<Boolean> value) {
        this.boxedBooleanPropertyWithGetterAndSetter = value;
    }

    public static class Builder {

        ValueWrapper<Boolean> booleanPropertyWithGetter = ValueWrapper.empty();
        ValueWrapper<Boolean> booleanPropertyWithGetterAndSetter = ValueWrapper.empty();
        ValueWrapper<Boolean> boxedBooleanPropertyWithGetter = ValueWrapper.empty();
        ValueWrapper<Boolean> boxedBooleanPropertyWithGetterAndSetter = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder booleanPropertyWithGetter(final ValueWrapper<Boolean> booleanPropertyWithGetter){
            this.booleanPropertyWithGetter = booleanPropertyWithGetter == null ? ValueWrapper.empty() : booleanPropertyWithGetter;
            return this;
        }

        public Builder booleanPropertyWithGetterAndSetter(final ValueWrapper<Boolean> booleanPropertyWithGetterAndSetter){
            this.booleanPropertyWithGetterAndSetter = booleanPropertyWithGetterAndSetter == null ? ValueWrapper.empty() : booleanPropertyWithGetterAndSetter;
            return this;
        }

        public Builder boxedBooleanPropertyWithGetter(final ValueWrapper<Boolean> boxedBooleanPropertyWithGetter){
            this.boxedBooleanPropertyWithGetter = boxedBooleanPropertyWithGetter == null ? ValueWrapper.empty() : boxedBooleanPropertyWithGetter;
            return this;
        }

        public Builder boxedBooleanPropertyWithGetterAndSetter(final ValueWrapper<Boolean> boxedBooleanPropertyWithGetterAndSetter){
            this.boxedBooleanPropertyWithGetterAndSetter = boxedBooleanPropertyWithGetterAndSetter == null ? ValueWrapper.empty() : boxedBooleanPropertyWithGetterAndSetter;
            return this;
        }


        public SourceClassBooleanGetterSetterDto build() {

            SourceClassBooleanGetterSetterDto instance = new SourceClassBooleanGetterSetterDto();
            instance.booleanPropertyWithGetter = booleanPropertyWithGetter;
            instance.booleanPropertyWithGetterAndSetter = booleanPropertyWithGetterAndSetter;
            instance.boxedBooleanPropertyWithGetter = boxedBooleanPropertyWithGetter;
            instance.boxedBooleanPropertyWithGetterAndSetter = boxedBooleanPropertyWithGetterAndSetter;

            return instance;

        }

    }

    @ProjectionFor(SourceClassBooleanGetterSetterDto.class)
    public static class Projection extends DtoProjection<SourceClassBooleanGetterSetterDto> {

        public FieldConf booleanPropertyWithGetter;
        public FieldConf booleanPropertyWithGetterAndSetter;
        public FieldConf boxedBooleanPropertyWithGetter;
        public FieldConf boxedBooleanPropertyWithGetterAndSetter;

        public Projection() {
        }

        public void applyProjectionTo(SourceClassBooleanGetterSetterDto dto) throws DtoProjectionException {
            dto.booleanPropertyWithGetter = ProjectionUtils.getProjectedValue(dto.booleanPropertyWithGetter, this.booleanPropertyWithGetter);
            dto.booleanPropertyWithGetterAndSetter = ProjectionUtils.getProjectedValue(dto.booleanPropertyWithGetterAndSetter, this.booleanPropertyWithGetterAndSetter);
            dto.boxedBooleanPropertyWithGetter = ProjectionUtils.getProjectedValue(dto.boxedBooleanPropertyWithGetter, this.boxedBooleanPropertyWithGetter);
            dto.boxedBooleanPropertyWithGetterAndSetter = ProjectionUtils.getProjectedValue(dto.boxedBooleanPropertyWithGetterAndSetter, this.boxedBooleanPropertyWithGetterAndSetter);
        }

        public static class Builder {

            FieldConf booleanPropertyWithGetter;
            FieldConf booleanPropertyWithGetterAndSetter;
            FieldConf boxedBooleanPropertyWithGetter;
            FieldConf boxedBooleanPropertyWithGetterAndSetter;

            public static Builder newInstance() {
                return new Builder();
            }

            public Builder booleanPropertyWithGetter(final FieldConf booleanPropertyWithGetter){
                this.booleanPropertyWithGetter = booleanPropertyWithGetter;
                return this;
            }

            public Builder booleanPropertyWithGetter(final Presence presence){
                booleanPropertyWithGetter = FieldConf.of(presence);
                return this;
            }

            public Builder booleanPropertyWithGetterAndSetter(final FieldConf booleanPropertyWithGetterAndSetter){
                this.booleanPropertyWithGetterAndSetter = booleanPropertyWithGetterAndSetter;
                return this;
            }

            public Builder booleanPropertyWithGetterAndSetter(final Presence presence){
                booleanPropertyWithGetterAndSetter = FieldConf.of(presence);
                return this;
            }

            public Builder boxedBooleanPropertyWithGetter(final FieldConf boxedBooleanPropertyWithGetter){
                this.boxedBooleanPropertyWithGetter = boxedBooleanPropertyWithGetter;
                return this;
            }

            public Builder boxedBooleanPropertyWithGetter(final Presence presence){
                boxedBooleanPropertyWithGetter = FieldConf.of(presence);
                return this;
            }

            public Builder boxedBooleanPropertyWithGetterAndSetter(final FieldConf boxedBooleanPropertyWithGetterAndSetter){
                this.boxedBooleanPropertyWithGetterAndSetter = boxedBooleanPropertyWithGetterAndSetter;
                return this;
            }

            public Builder boxedBooleanPropertyWithGetterAndSetter(final Presence presence){
                boxedBooleanPropertyWithGetterAndSetter = FieldConf.of(presence);
                return this;
            }

            public SourceClassBooleanGetterSetterDto.Projection build() {

                SourceClassBooleanGetterSetterDto.Projection instance = new SourceClassBooleanGetterSetterDto.Projection();
                instance.booleanPropertyWithGetter = booleanPropertyWithGetter;
                instance.booleanPropertyWithGetterAndSetter = booleanPropertyWithGetterAndSetter;
                instance.boxedBooleanPropertyWithGetter = boxedBooleanPropertyWithGetter;
                instance.boxedBooleanPropertyWithGetterAndSetter = boxedBooleanPropertyWithGetterAndSetter;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _booleanPropertyWithGetter = "booleanPropertyWithGetter";
        public static final String _booleanPropertyWithGetterAndSetter = "booleanPropertyWithGetterAndSetter";
        public static final String _boxedBooleanPropertyWithGetter = "boxedBooleanPropertyWithGetter";
        public static final String _boxedBooleanPropertyWithGetterAndSetter = "boxedBooleanPropertyWithGetterAndSetter";

    }

}