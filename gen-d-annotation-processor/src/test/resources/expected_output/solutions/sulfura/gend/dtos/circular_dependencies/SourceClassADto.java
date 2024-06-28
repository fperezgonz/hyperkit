package solutions.sulfura.gend.dtos.circular_dependencies;

import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.conf.DtoConf;
import solutions.sulfura.gend.dtos.conf.fields.DtoListFieldConf;
import solutions.sulfura.gend.dtos.conf.fields.DtoFieldConf;
import solutions.sulfura.gend.dtos.conf.fields.ListFieldConf;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassBDto;
import java.util.List;
import java.util.ArrayList;
import solutions.sulfura.gend.dtos.ListOperation;
import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassA;

@DtoFor(SourceClassA.class)
public class SourceClassADto implements Dto<SourceClassA>{

    public List<ListOperation<SourceClassBDto>> propertyArray = new ArrayList<>();
    public Option<SourceClassBDto> property = Option.some(null);
    public List<ListOperation<List<ListOperation<SourceClassBDto>>>> genericPropertyArray = new ArrayList<>();
    public List<ListOperation<SourceClassBDto>> genericProperty = new ArrayList<>();

    public SourceClassADto(){}

    public Class<SourceClassA> getSourceClass() {
        return SourceClassA.class;
    }

    public static class Builder{

        public List<ListOperation<SourceClassBDto>> propertyArray = new ArrayList<>();
        public Option<SourceClassBDto> property = Option.some(null);
        public List<ListOperation<List<ListOperation<SourceClassBDto>>>> genericPropertyArray = new ArrayList<>();
        public List<ListOperation<SourceClassBDto>> genericProperty = new ArrayList<>();

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder propertyArray(List<ListOperation<SourceClassBDto>> propertyArray){
            this.propertyArray = propertyArray == null ? new ArrayList<>() : propertyArray;
            return this;
        }

        public Builder property(Option<SourceClassBDto> property){
            this.property = property == null ? Option.some(null) : property;
            return this;
        }

        public Builder genericPropertyArray(List<ListOperation<List<ListOperation<SourceClassBDto>>>> genericPropertyArray){
            this.genericPropertyArray = genericPropertyArray == null ? new ArrayList<>() : genericPropertyArray;
            return this;
        }

        public Builder genericProperty(List<ListOperation<SourceClassBDto>> genericProperty){
            this.genericProperty = genericProperty == null ? new ArrayList<>() : genericProperty;
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

    public static class Conf extends DtoConf<SourceClassADto>{

        public DtoListFieldConf<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassBDto.Conf> propertyArray;
        public DtoFieldConf<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassBDto.Conf> property;
        public ListFieldConf genericPropertyArray;
        public DtoListFieldConf<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassBDto.Conf> genericProperty;

        public Conf(){}

        public static class Builder{

            public DtoListFieldConf<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassBDto.Conf> propertyArray;
            public DtoFieldConf<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassBDto.Conf> property;
            public ListFieldConf genericPropertyArray;
            public DtoListFieldConf<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassBDto.Conf> genericProperty;

            public static  Builder newInstance(){
                return new Builder();
            }

            public Builder propertyArray(DtoListFieldConf<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassBDto.Conf> propertyArray){
                this.propertyArray = propertyArray;
                return this;
            }

            public Builder property(DtoFieldConf<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassBDto.Conf> property){
                this.property = property;
                return this;
            }

            public Builder genericPropertyArray(ListFieldConf genericPropertyArray){
                this.genericPropertyArray = genericPropertyArray;
                return this;
            }

            public Builder genericProperty(DtoListFieldConf<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassBDto.Conf> genericProperty){
                this.genericProperty = genericProperty;
                return this;
            }

            public SourceClassADto.Conf build(){
                SourceClassADto.Conf instance = new SourceClassADto.Conf();
                instance.propertyArray = propertyArray;
                instance.property = property;
                instance.genericPropertyArray = genericPropertyArray;
                instance.genericProperty = genericProperty;
                return instance;
            }

        }


    }

}