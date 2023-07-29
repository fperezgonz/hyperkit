package solutions.sulfura.gend.dtos.circular_dependencies;

import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassB;
import java.util.List;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassADto;
import solutions.sulfura.gend.dtos.ListOperation;

@DtoFor(SourceClassB.class)
public class SourceClassBDto implements Dto<SourceClassB>{

    public Option<List<ListOperation<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassADto>>> propertyArray;
    public Option<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassA> property;
    public Option<List<ListOperation<List<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassA>>>> genericPropertyArray;
    public Option<List<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassA>> genericProperty;

    public SourceClassBDto(){}

    public static  Builder builder(){
        return new Builder();
    }

    public static class Builder{

        public Option<List<ListOperation<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassADto>>> propertyArray;
        public Option<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassA> property;
        public Option<List<ListOperation<List<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassA>>>> genericPropertyArray;
        public Option<List<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassA>> genericProperty;

        public Builder propertyArray(Option<List<ListOperation<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassADto>>> propertyArray){
            this.propertyArray = propertyArray;
            return this;
        }

        public Builder property(Option<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassA> property){
            this.property = property;
            return this;
        }

        public Builder genericPropertyArray(Option<List<ListOperation<List<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassA>>>> genericPropertyArray){
            this.genericPropertyArray = genericPropertyArray;
            return this;
        }

        public Builder genericProperty(Option<List<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassA>> genericProperty){
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