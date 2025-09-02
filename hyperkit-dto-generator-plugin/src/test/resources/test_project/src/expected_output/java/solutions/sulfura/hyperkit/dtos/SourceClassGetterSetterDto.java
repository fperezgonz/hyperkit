package solutions.sulfura.hyperkit.dtos;

import solutions.sulfura.hyperkit.dtos.SourceClassGetterSetter;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;

@DtoFor(SourceClassGetterSetter.class)
public class SourceClassGetterSetterDto implements Dto<SourceClassGetterSetter> {

    public ValueWrapper<String> stringPropertyWithGetter = ValueWrapper.empty();
    public ValueWrapper<String> stringPropertyWithGetterAndSetter = ValueWrapper.empty();
    public ValueWrapper<String> stringPropertyWithSetter = ValueWrapper.empty();

    public SourceClassGetterSetterDto() {
    }

    public Class<SourceClassGetterSetter> getSourceClass() {
        return SourceClassGetterSetter.class;
    }

    public static class Builder {

        ValueWrapper<String> stringPropertyWithGetter = ValueWrapper.empty();
        ValueWrapper<String> stringPropertyWithGetterAndSetter = ValueWrapper.empty();
        ValueWrapper<String> stringPropertyWithSetter = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder stringPropertyWithGetter(final ValueWrapper<String> stringPropertyWithGetter){
            this.stringPropertyWithGetter = stringPropertyWithGetter == null ? ValueWrapper.empty() : stringPropertyWithGetter;
            return this;
        }

        public Builder stringPropertyWithGetterAndSetter(final ValueWrapper<String> stringPropertyWithGetterAndSetter){
            this.stringPropertyWithGetterAndSetter = stringPropertyWithGetterAndSetter == null ? ValueWrapper.empty() : stringPropertyWithGetterAndSetter;
            return this;
        }

        public Builder stringPropertyWithSetter(final ValueWrapper<String> stringPropertyWithSetter){
            this.stringPropertyWithSetter = stringPropertyWithSetter == null ? ValueWrapper.empty() : stringPropertyWithSetter;
            return this;
        }


        public SourceClassGetterSetterDto build() {

            SourceClassGetterSetterDto instance = new SourceClassGetterSetterDto();
            instance.stringPropertyWithGetter = stringPropertyWithGetter;
            instance.stringPropertyWithGetterAndSetter = stringPropertyWithGetterAndSetter;
            instance.stringPropertyWithSetter = stringPropertyWithSetter;

            return instance;

        }

    }

    @ProjectionFor(SourceClassGetterSetterDto.class)
    public static class Projection extends DtoProjection<SourceClassGetterSetterDto> {

        public FieldConf stringPropertyWithGetter;
        public FieldConf stringPropertyWithGetterAndSetter;
        public FieldConf stringPropertyWithSetter;

        public Projection() {
        }

        public void applyProjectionTo(SourceClassGetterSetterDto dto) throws DtoProjectionException {
            dto.stringPropertyWithGetter = ProjectionUtils.getProjectedValue(dto.stringPropertyWithGetter, this.stringPropertyWithGetter);
            dto.stringPropertyWithGetterAndSetter = ProjectionUtils.getProjectedValue(dto.stringPropertyWithGetterAndSetter, this.stringPropertyWithGetterAndSetter);
            dto.stringPropertyWithSetter = ProjectionUtils.getProjectedValue(dto.stringPropertyWithSetter, this.stringPropertyWithSetter);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return stringPropertyWithGetter.equals(that.stringPropertyWithGetter)
                       && stringPropertyWithGetterAndSetter.equals(that.stringPropertyWithGetterAndSetter)
                       && stringPropertyWithSetter.equals(that.stringPropertyWithSetter);

        }

        public static class Builder {

            FieldConf stringPropertyWithGetter;
            FieldConf stringPropertyWithGetterAndSetter;
            FieldConf stringPropertyWithSetter;

            public static Builder newInstance() {
                return new Builder();
            }

            public Builder stringPropertyWithGetter(final FieldConf stringPropertyWithGetter){
                this.stringPropertyWithGetter = stringPropertyWithGetter;
                return this;
            }

            public Builder stringPropertyWithGetter(final Presence presence){
                stringPropertyWithGetter = FieldConf.of(presence);
                return this;
            }

            public Builder stringPropertyWithGetterAndSetter(final FieldConf stringPropertyWithGetterAndSetter){
                this.stringPropertyWithGetterAndSetter = stringPropertyWithGetterAndSetter;
                return this;
            }

            public Builder stringPropertyWithGetterAndSetter(final Presence presence){
                stringPropertyWithGetterAndSetter = FieldConf.of(presence);
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

            public SourceClassGetterSetterDto.Projection build() {

                SourceClassGetterSetterDto.Projection instance = new SourceClassGetterSetterDto.Projection();
                instance.stringPropertyWithGetter = stringPropertyWithGetter;
                instance.stringPropertyWithGetterAndSetter = stringPropertyWithGetterAndSetter;
                instance.stringPropertyWithSetter = stringPropertyWithSetter;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _stringPropertyWithGetter = "stringPropertyWithGetter";
        public static final String _stringPropertyWithGetterAndSetter = "stringPropertyWithGetterAndSetter";
        public static final String _stringPropertyWithSetter = "stringPropertyWithSetter";

    }

}