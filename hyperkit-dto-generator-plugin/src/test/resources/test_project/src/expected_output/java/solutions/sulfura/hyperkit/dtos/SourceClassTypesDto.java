package solutions.sulfura.hyperkit.dtos;

import solutions.sulfura.hyperkit.dtos.SourceClassTypes;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import java.util.List;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.fields.ListFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;

@DtoFor(SourceClassTypes.class)
public class SourceClassTypesDto implements Dto<SourceClassTypes> {

    public ValueWrapper<String> stringProperty = ValueWrapper.empty();
    public ValueWrapper<Long> longProperty = ValueWrapper.empty();
    public ValueWrapper<Boolean> booleanProperty = ValueWrapper.empty();
    public ValueWrapper<Double> doubleProperty = ValueWrapper.empty();
    public ValueWrapper<List<ListOperation<String>>> stringArrayProperty = ValueWrapper.empty();
    public ValueWrapper<List<ListOperation<Boolean>>> booleanArrayProperty = ValueWrapper.empty();

    public SourceClassTypesDto() {
    }

    public Class<SourceClassTypes> getSourceClass() {
        return SourceClassTypes.class;
    }

    public static class Builder {

        ValueWrapper<String> stringProperty = ValueWrapper.empty();
        ValueWrapper<Long> longProperty = ValueWrapper.empty();
        ValueWrapper<Boolean> booleanProperty = ValueWrapper.empty();
        ValueWrapper<Double> doubleProperty = ValueWrapper.empty();
        ValueWrapper<List<ListOperation<String>>> stringArrayProperty = ValueWrapper.empty();
        ValueWrapper<List<ListOperation<Boolean>>> booleanArrayProperty = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder stringProperty(final ValueWrapper<String> stringProperty){
            this.stringProperty = stringProperty == null ? ValueWrapper.empty() : stringProperty;
            return this;
        }

        public Builder longProperty(final ValueWrapper<Long> longProperty){
            this.longProperty = longProperty == null ? ValueWrapper.empty() : longProperty;
            return this;
        }

        public Builder booleanProperty(final ValueWrapper<Boolean> booleanProperty){
            this.booleanProperty = booleanProperty == null ? ValueWrapper.empty() : booleanProperty;
            return this;
        }

        public Builder doubleProperty(final ValueWrapper<Double> doubleProperty){
            this.doubleProperty = doubleProperty == null ? ValueWrapper.empty() : doubleProperty;
            return this;
        }

        public Builder stringArrayProperty(final ValueWrapper<List<ListOperation<String>>> stringArrayProperty){
            this.stringArrayProperty = stringArrayProperty == null ? ValueWrapper.empty() : stringArrayProperty;
            return this;
        }

        public Builder booleanArrayProperty(final ValueWrapper<List<ListOperation<Boolean>>> booleanArrayProperty){
            this.booleanArrayProperty = booleanArrayProperty == null ? ValueWrapper.empty() : booleanArrayProperty;
            return this;
        }


        public SourceClassTypesDto build() {

            SourceClassTypesDto instance = new SourceClassTypesDto();
            instance.stringProperty = stringProperty;
            instance.longProperty = longProperty;
            instance.booleanProperty = booleanProperty;
            instance.doubleProperty = doubleProperty;
            instance.stringArrayProperty = stringArrayProperty;
            instance.booleanArrayProperty = booleanArrayProperty;

            return instance;

        }

    }

    @ProjectionFor(SourceClassTypesDto.class)
    public static class Projection extends DtoProjection<SourceClassTypesDto> {

        public FieldConf stringProperty;
        public FieldConf longProperty;
        public FieldConf booleanProperty;
        public FieldConf doubleProperty;
        public ListFieldConf stringArrayProperty;
        public ListFieldConf booleanArrayProperty;

        public Projection() {
        }

        public void applyProjectionTo(SourceClassTypesDto dto) throws DtoProjectionException {
            dto.stringProperty = ProjectionUtils.getProjectedValue(dto.stringProperty, this.stringProperty);
            dto.longProperty = ProjectionUtils.getProjectedValue(dto.longProperty, this.longProperty);
            dto.booleanProperty = ProjectionUtils.getProjectedValue(dto.booleanProperty, this.booleanProperty);
            dto.doubleProperty = ProjectionUtils.getProjectedValue(dto.doubleProperty, this.doubleProperty);
            dto.stringArrayProperty = ProjectionUtils.getProjectedValue(dto.stringArrayProperty, this.stringArrayProperty);
            dto.booleanArrayProperty = ProjectionUtils.getProjectedValue(dto.booleanArrayProperty, this.booleanArrayProperty);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return stringProperty.equals(that.stringProperty)
                       && longProperty.equals(that.longProperty)
                       && booleanProperty.equals(that.booleanProperty)
                       && doubleProperty.equals(that.doubleProperty)
                       && stringArrayProperty.equals(that.stringArrayProperty)
                       && booleanArrayProperty.equals(that.booleanArrayProperty);

        }

        public static class Builder {

            FieldConf stringProperty;
            FieldConf longProperty;
            FieldConf booleanProperty;
            FieldConf doubleProperty;
            ListFieldConf stringArrayProperty;
            ListFieldConf booleanArrayProperty;

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

            public Builder longProperty(final FieldConf longProperty){
                this.longProperty = longProperty;
                return this;
            }

            public Builder longProperty(final Presence presence){
                longProperty = FieldConf.of(presence);
                return this;
            }

            public Builder booleanProperty(final FieldConf booleanProperty){
                this.booleanProperty = booleanProperty;
                return this;
            }

            public Builder booleanProperty(final Presence presence){
                booleanProperty = FieldConf.of(presence);
                return this;
            }

            public Builder doubleProperty(final FieldConf doubleProperty){
                this.doubleProperty = doubleProperty;
                return this;
            }

            public Builder doubleProperty(final Presence presence){
                doubleProperty = FieldConf.of(presence);
                return this;
            }

            public Builder stringArrayProperty(final ListFieldConf stringArrayProperty){
                this.stringArrayProperty = stringArrayProperty;
                return this;
            }

            public Builder stringArrayProperty(final Presence presence){
                stringArrayProperty = ListFieldConf.of(presence);
                return this;
            }

            public Builder booleanArrayProperty(final ListFieldConf booleanArrayProperty){
                this.booleanArrayProperty = booleanArrayProperty;
                return this;
            }

            public Builder booleanArrayProperty(final Presence presence){
                booleanArrayProperty = ListFieldConf.of(presence);
                return this;
            }

            public SourceClassTypesDto.Projection build() {

                SourceClassTypesDto.Projection instance = new SourceClassTypesDto.Projection();
                instance.stringProperty = stringProperty;
                instance.longProperty = longProperty;
                instance.booleanProperty = booleanProperty;
                instance.doubleProperty = doubleProperty;
                instance.stringArrayProperty = stringArrayProperty;
                instance.booleanArrayProperty = booleanArrayProperty;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _stringProperty = "stringProperty";
        public static final String _longProperty = "longProperty";
        public static final String _booleanProperty = "booleanProperty";
        public static final String _doubleProperty = "doubleProperty";
        public static final String _stringArrayProperty = "stringArrayProperty";
        public static final String _booleanArrayProperty = "booleanArrayProperty";

    }

}