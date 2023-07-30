package solutions.sulfura.gend.dtos.circular_dependencies;

import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassB;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassADto;
import java.util.List;
import solutions.sulfura.gend.dtos.ListOperation;

@DtoFor(SourceClassB.class)
public class SourceClassBDto implements Dto<SourceClassB>{

    public Option<List<ListOperation<SourceClassADto>>> propertyArray;
    public Option<SourceClassADto> property;
    public Option<List<ListOperation<List<ListOperation<SourceClassADto>>>>> genericPropertyArray;
    public Option<List<ListOperation<SourceClassADto>>> genericProperty;

    public SourceClassBDto(){}

    public static class Builder{

        public Option<List<ListOperation<SourceClassADto>>> propertyArray;
        public Option<SourceClassADto> property;
        public Option<List<ListOperation<List<ListOperation<SourceClassADto>>>>> genericPropertyArray;
        public Option<List<ListOperation<SourceClassADto>>> genericProperty;

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder propertyArray(Option<List<ListOperation<SourceClassADto>>> propertyArray){
            this.propertyArray = propertyArray;
            return this;
        }

        public Builder property(Option<SourceClassADto> property){
            this.property = property;
            return this;
        }

        public Builder genericPropertyArray(Option<List<ListOperation<List<ListOperation<SourceClassADto>>>>> genericPropertyArray){
            this.genericPropertyArray = genericPropertyArray;
            return this;
        }

        public Builder genericProperty(Option<List<ListOperation<SourceClassADto>>> genericProperty){
            this.genericProperty = genericProperty;
            return this;
        }

        public SourceClassBDto build(){
            SourceClassBDto instance = new SourceClassBDto();
            instance.propertyArray = propertyArray;
            instance.property = property;
            instance.genericPropertyArray = genericPropertyArray;
            instance.genericProperty = genericProperty;
            return instance;
        }

    }

}