package solutions.sulfura.gend.dtos;

import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.SourceClassTypes;
import java.lang.String;
import java.util.List;
import solutions.sulfura.gend.dtos.ListOperation;

@DtoFor(SourceClassTypes.class)
public class SourceClassTypesDto implements Dto<SourceClassTypes>{

    public Option<List<ListOperation<String>>> stringArrayProperty;
    public Option<Boolean> booleanProperty;
    public Option<Double> doubleProperty;
    public Option<Long> longProperty;
    public Option<List<ListOperation<Boolean>>> booleanArrayProperty;
    public Option<String> stringProperty;

    public SourceClassTypesDto(){}

    public static class Builder{

        public Option<List<ListOperation<String>>> stringArrayProperty;
        public Option<Boolean> booleanProperty;
        public Option<Double> doubleProperty;
        public Option<Long> longProperty;
        public Option<List<ListOperation<Boolean>>> booleanArrayProperty;
        public Option<String> stringProperty;

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder stringArrayProperty(Option<List<ListOperation<String>>> stringArrayProperty){
            this.stringArrayProperty = stringArrayProperty;
            return this;
        }

        public Builder booleanProperty(Option<Boolean> booleanProperty){
            this.booleanProperty = booleanProperty;
            return this;
        }

        public Builder doubleProperty(Option<Double> doubleProperty){
            this.doubleProperty = doubleProperty;
            return this;
        }

        public Builder longProperty(Option<Long> longProperty){
            this.longProperty = longProperty;
            return this;
        }

        public Builder booleanArrayProperty(Option<List<ListOperation<Boolean>>> booleanArrayProperty){
            this.booleanArrayProperty = booleanArrayProperty;
            return this;
        }

        public Builder stringProperty(Option<String> stringProperty){
            this.stringProperty = stringProperty;
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

}