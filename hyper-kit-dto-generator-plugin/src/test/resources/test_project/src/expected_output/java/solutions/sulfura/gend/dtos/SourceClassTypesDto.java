package solutions.sulfura.gend.dtos;

import solutions.sulfura.gend.dtos.SourceClassTypes;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.projection.fields.FieldConf;
import solutions.sulfura.gend.dtos.projection.DtoProjectionException;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.projection.ProjectionFor;
import solutions.sulfura.gend.dtos.ListOperation;
import java.util.List;
import solutions.sulfura.gend.dtos.projection.DtoProjection;
import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.projection.fields.ListFieldConf;
import solutions.sulfura.gend.dtos.projection.ProjectionUtils;
import solutions.sulfura.gend.dtos.projection.fields.FieldConf.Presence;

@DtoFor(SourceClassTypes.class)
public class SourceClassTypesDto implements Dto<SourceClassTypes>{

    public Option<String> stringProperty = Option.none();
    public Option<Long> longProperty = Option.none();
    public Option<Boolean> booleanProperty = Option.none();
    public Option<Double> doubleProperty = Option.none();
    public Option<List<ListOperation<String>>> stringArrayProperty = Option.none();
    public Option<List<ListOperation<Boolean>>> booleanArrayProperty = Option.none();

    public SourceClassTypesDto() {
    }

    public Class<SourceClassTypes> getSourceClass() {
        return SourceClassTypes.class;
    }

    public static class Builder{

        Option<String> stringProperty = Option.none();
        Option<Long> longProperty = Option.none();
        Option<Boolean> booleanProperty = Option.none();
        Option<Double> doubleProperty = Option.none();
        Option<List<ListOperation<String>>> stringArrayProperty = Option.none();
        Option<List<ListOperation<Boolean>>> booleanArrayProperty = Option.none();

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder stringProperty(final Option<String> stringProperty){
            this.stringProperty = stringProperty == null ? Option.none() : stringProperty;
            return this;
        }

        public Builder longProperty(final Option<Long> longProperty){
            this.longProperty = longProperty == null ? Option.none() : longProperty;
            return this;
        }

        public Builder booleanProperty(final Option<Boolean> booleanProperty){
            this.booleanProperty = booleanProperty == null ? Option.none() : booleanProperty;
            return this;
        }

        public Builder doubleProperty(final Option<Double> doubleProperty){
            this.doubleProperty = doubleProperty == null ? Option.none() : doubleProperty;
            return this;
        }

        public Builder stringArrayProperty(final Option<List<ListOperation<String>>> stringArrayProperty){
            this.stringArrayProperty = stringArrayProperty == null ? Option.none() : stringArrayProperty;
            return this;
        }

        public Builder booleanArrayProperty(final Option<List<ListOperation<Boolean>>> booleanArrayProperty){
            this.booleanArrayProperty = booleanArrayProperty == null ? Option.none() : booleanArrayProperty;
            return this;
        }


        public SourceClassTypesDto build(){

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

        public static class Builder{

            FieldConf stringProperty;
            FieldConf longProperty;
            FieldConf booleanProperty;
            FieldConf doubleProperty;
            ListFieldConf stringArrayProperty;
            ListFieldConf booleanArrayProperty;

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

            public SourceClassTypesDto.Projection build(){

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

    public static class DtoModel{

        public static final String _stringProperty = "stringProperty";
        public static final String _longProperty = "longProperty";
        public static final String _booleanProperty = "booleanProperty";
        public static final String _doubleProperty = "doubleProperty";
        public static final String _stringArrayProperty = "stringArrayProperty";
        public static final String _booleanArrayProperty = "booleanArrayProperty";

    }

}