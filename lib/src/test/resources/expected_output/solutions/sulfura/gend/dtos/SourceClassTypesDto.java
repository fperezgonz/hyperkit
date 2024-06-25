package solutions.sulfura.gend.dtos;

import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.conf.DtoConf;
import solutions.sulfura.gend.dtos.conf.fields.ListFieldConf;
import solutions.sulfura.gend.dtos.conf.fields.FieldConf;
import java.lang.String;
import java.util.List;
import solutions.sulfura.gend.dtos.ListOperation;
import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.SourceClassTypes;

@DtoFor(SourceClassTypes.class)
public class SourceClassTypesDto implements Dto<SourceClassTypes>{

    public Option<List<ListOperation<String>>> stringArrayProperty = Option.some(null);
    public Option<Boolean> booleanProperty = Option.some(null);
    public Option<Double> doubleProperty = Option.some(null);
    public Option<Long> longProperty = Option.some(null);
    public Option<List<ListOperation<Boolean>>> booleanArrayProperty = Option.some(null);
    public Option<String> stringProperty = Option.some(null);

    public SourceClassTypesDto(){}

    public static class Builder{

        public Option<List<ListOperation<String>>> stringArrayProperty = Option.some(null);
        public Option<Boolean> booleanProperty = Option.some(null);
        public Option<Double> doubleProperty = Option.some(null);
        public Option<Long> longProperty = Option.some(null);
        public Option<List<ListOperation<Boolean>>> booleanArrayProperty = Option.some(null);
        public Option<String> stringProperty = Option.some(null);

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder stringArrayProperty(Option<List<ListOperation<String>>> stringArrayProperty){
            this.stringArrayProperty = stringArrayProperty == null ? Option.some(null) : stringArrayProperty;
            return this;
        }

        public Builder booleanProperty(Option<Boolean> booleanProperty){
            this.booleanProperty = booleanProperty == null ? Option.some(null) : booleanProperty;
            return this;
        }

        public Builder doubleProperty(Option<Double> doubleProperty){
            this.doubleProperty = doubleProperty == null ? Option.some(null) : doubleProperty;
            return this;
        }

        public Builder longProperty(Option<Long> longProperty){
            this.longProperty = longProperty == null ? Option.some(null) : longProperty;
            return this;
        }

        public Builder booleanArrayProperty(Option<List<ListOperation<Boolean>>> booleanArrayProperty){
            this.booleanArrayProperty = booleanArrayProperty == null ? Option.some(null) : booleanArrayProperty;
            return this;
        }

        public Builder stringProperty(Option<String> stringProperty){
            this.stringProperty = stringProperty == null ? Option.some(null) : stringProperty;
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

    public static class Conf extends DtoConf<SourceClassTypesDto>{

        public ListFieldConf stringArrayProperty;
        public FieldConf booleanProperty;
        public FieldConf doubleProperty;
        public FieldConf longProperty;
        public ListFieldConf booleanArrayProperty;
        public FieldConf stringProperty;

        public Conf(){}

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

            public SourceClassTypesDto.Conf build(){
                SourceClassTypesDto.Conf instance = new SourceClassTypesDto.Conf();
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