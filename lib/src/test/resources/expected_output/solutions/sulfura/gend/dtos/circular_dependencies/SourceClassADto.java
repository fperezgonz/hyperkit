package solutions.sulfura.gend.dtos.circular_dependencies;

import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassA;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassBDto;
import java.util.List;
import solutions.sulfura.gend.dtos.ListOperation;

@DtoFor(SourceClassA.class)
public class SourceClassADto implements Dto<SourceClassA>{

    public Option<List<ListOperation<SourceClassBDto>>> propertyArray;
    public Option<SourceClassBDto> property;
    public Option<List<ListOperation<List<ListOperation<SourceClassBDto>>>>> genericPropertyArray;
    public Option<List<ListOperation<SourceClassBDto>>> genericProperty;

    public SourceClassADto(){}

    public static  Builder builder(){
        return new Builder();
    }

    public static class Builder{

        public Option<List<ListOperation<SourceClassBDto>>> propertyArray;
        public Option<SourceClassBDto> property;
        public Option<List<ListOperation<List<ListOperation<SourceClassBDto>>>>> genericPropertyArray;
        public Option<List<ListOperation<SourceClassBDto>>> genericProperty;

        public Builder propertyArray(Option<List<ListOperation<SourceClassBDto>>> propertyArray){
            this.propertyArray = propertyArray;
            return this;
        }

        public Builder property(Option<SourceClassBDto> property){
            this.property = property;
            return this;
        }

        public Builder genericPropertyArray(Option<List<ListOperation<List<ListOperation<SourceClassBDto>>>>> genericPropertyArray){
            this.genericPropertyArray = genericPropertyArray;
            return this;
        }

        public Builder genericProperty(Option<List<ListOperation<SourceClassBDto>>> genericProperty){
            this.genericProperty = genericProperty;
            return this;
        }

        public SourceClassADto build(){
            SourceClassADto instance = new SourceClassADto();
            instance.propertyArray = propertyArray;
            instance.property = property;
            instance.genericPropertyArray = genericPropertyArray;
            instance.genericProperty = genericProperty;
            return instance;
        }

    }

}