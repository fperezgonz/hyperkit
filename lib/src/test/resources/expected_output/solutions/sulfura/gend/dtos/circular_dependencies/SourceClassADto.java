package solutions.sulfura.gend.dtos.circular_dependencies;

import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassA;
import java.util.List;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassBDto;
import solutions.sulfura.gend.dtos.ListOperation;

@DtoFor(SourceClassA.class)
public class SourceClassADto implements Dto<SourceClassA>{

    public Option<List<ListOperation<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassBDto>>> propertyArray;
    public Option<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassB> property;
    public Option<List<ListOperation<List<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassB>>>> genericPropertyArray;
    public Option<List<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassB>> genericProperty;

    public SourceClassADto(){}

    public static  Builder builder(){
        return new Builder();
    }

    public static class Builder{

        public Option<List<ListOperation<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassBDto>>> propertyArray;
        public Option<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassB> property;
        public Option<List<ListOperation<List<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassB>>>> genericPropertyArray;
        public Option<List<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassB>> genericProperty;

        public Builder propertyArray(Option<List<ListOperation<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassBDto>>> propertyArray){
            this.propertyArray = propertyArray;
            return this;
        }

        public Builder property(Option<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassB> property){
            this.property = property;
            return this;
        }

        public Builder genericPropertyArray(Option<List<ListOperation<List<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassB>>>> genericPropertyArray){
            this.genericPropertyArray = genericPropertyArray;
            return this;
        }

        public Builder genericProperty(Option<List<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassB>> genericProperty){
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