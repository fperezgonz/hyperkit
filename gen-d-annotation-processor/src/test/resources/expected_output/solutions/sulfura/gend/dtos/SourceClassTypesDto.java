package solutions.sulfura.gend.dtos;

import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.projection.DtoProjection;
import solutions.sulfura.gend.dtos.projection.fields.ListFieldConf;
import solutions.sulfura.gend.dtos.projection.fields.FieldConf;
import java.lang.String;
import java.util.List;
import solutions.sulfura.gend.dtos.ListOperation;
import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.SourceClassTypes;

@DtoFor(SourceClassTypes.class)
public class SourceClassTypesDto implements Dto<SourceClassTypes>{

    public Option<List<ListOperation<String>>> stringArrayProperty = Option.none();
    public Option<Boolean> booleanProperty = Option.none();
    public Option<Double> doubleProperty = Option.none();
    public Option<Long> longProperty = Option.none();
    public Option<List<ListOperation<Boolean>>> booleanArrayProperty = Option.none();
    public Option<String> stringProperty = Option.none();

    public SourceClassTypesDto(){}

    public Class<SourceClassTypes> getSourceClass() {
        return SourceClassTypes.class;
    }

    public static class Builder{

        public Option<List<ListOperation<String>>> stringArrayProperty = Option.none();
        public Option<Boolean> booleanProperty = Option.none();
        public Option<Double> doubleProperty = Option.none();
        public Option<Long> longProperty = Option.none();
        public Option<List<ListOperation<Boolean>>> booleanArrayProperty = Option.none();
        public Option<String> stringProperty = Option.none();

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder stringArrayProperty(Option<List<ListOperation<String>>> stringArrayProperty){
            this.stringArrayProperty = stringArrayProperty == null ? Option.none() : stringArrayProperty;
            return this;
        }

        public Builder booleanProperty(Option<Boolean> booleanProperty){
            this.booleanProperty = booleanProperty == null ? Option.none() : booleanProperty;
            return this;
        }

        public Builder doubleProperty(Option<Double> doubleProperty){
            this.doubleProperty = doubleProperty == null ? Option.none() : doubleProperty;
            return this;
        }

        public Builder longProperty(Option<Long> longProperty){
            this.longProperty = longProperty == null ? Option.none() : longProperty;
            return this;
        }

        public Builder booleanArrayProperty(Option<List<ListOperation<Boolean>>> booleanArrayProperty){
            this.booleanArrayProperty = booleanArrayProperty == null ? Option.none() : booleanArrayProperty;
            return this;
        }

        public Builder stringProperty(Option<String> stringProperty){
            this.stringProperty = stringProperty == null ? Option.none() : stringProperty;
            return this;
        }

        public SourceClassTypesDto build(){
            SourceClassTypesDto instance = new SourceClassTypesDto();
            instance.stringArrayProperty = stringArrayProperty;
            instance.booleanProperty = booleanProperty;
            instance.doubleProperty = doubleProperty;
            instance.longProperty = longProperty;
            instance.booleanArrayProperty = booleanArrayProperty;
            instance.stringProperty = stringProperty;
            return instance;
        }

    }

    public static class Projection extends DtoProjection<SourceClassTypesDto>{

        public ListFieldConf stringArrayProperty;
        public FieldConf booleanProperty;
        public FieldConf doubleProperty;
        public FieldConf longProperty;
        public ListFieldConf booleanArrayProperty;
        public FieldConf stringProperty;

        public Projection(){}

        public static class Builder{

            public ListFieldConf stringArrayProperty;
            public FieldConf booleanProperty;
            public FieldConf doubleProperty;
            public FieldConf longProperty;
            public ListFieldConf booleanArrayProperty;
            public FieldConf stringProperty;

            public static  Builder newInstance(){
                return new Builder();
            }

            public Builder stringArrayProperty(ListFieldConf stringArrayProperty){
                this.stringArrayProperty = stringArrayProperty;
                return this;
            }

            public Builder booleanProperty(FieldConf booleanProperty){
                this.booleanProperty = booleanProperty;
                return this;
            }

            public Builder doubleProperty(FieldConf doubleProperty){
                this.doubleProperty = doubleProperty;
                return this;
            }

            public Builder longProperty(FieldConf longProperty){
                this.longProperty = longProperty;
                return this;
            }

            public Builder booleanArrayProperty(ListFieldConf booleanArrayProperty){
                this.booleanArrayProperty = booleanArrayProperty;
                return this;
            }

            public Builder stringProperty(FieldConf stringProperty){
                this.stringProperty = stringProperty;
                return this;
            }

            public SourceClassTypesDto.Projection build(){
                SourceClassTypesDto.Projection instance = new SourceClassTypesDto.Projection();
                instance.stringArrayProperty = stringArrayProperty;
                instance.booleanProperty = booleanProperty;
                instance.doubleProperty = doubleProperty;
                instance.longProperty = longProperty;
                instance.booleanArrayProperty = booleanArrayProperty;
                instance.stringProperty = stringProperty;
                return instance;
            }

        }


    }

}