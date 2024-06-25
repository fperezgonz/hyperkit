package solutions.sulfura.gend.dtos.circular_dependencies;

import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.conf.DtoConf;
import solutions.sulfura.gend.dtos.conf.fields.DtoListFieldConf;
import solutions.sulfura.gend.dtos.conf.fields.DtoFieldConf;
import solutions.sulfura.gend.dtos.conf.fields.ListFieldConf;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassADto;
import java.util.List;
import java.util.ArrayList;
import solutions.sulfura.gend.dtos.ListOperation;
import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassB;

@DtoFor(SourceClassB.class)
public class SourceClassBDto implements Dto<SourceClassB>{

    public List<ListOperation<SourceClassADto>> propertyArray = new ArrayList<>();
    public Option<SourceClassADto> property = Option.some(null);
    public List<ListOperation<List<ListOperation<SourceClassADto>>>> genericPropertyArray = new ArrayList<>();
    public List<ListOperation<SourceClassADto>> genericProperty = new ArrayList<>();

    public SourceClassBDto(){}

    public static class Builder{

        public List<ListOperation<SourceClassADto>> propertyArray = new ArrayList<>();
        public Option<SourceClassADto> property = Option.some(null);
        public List<ListOperation<List<ListOperation<SourceClassADto>>>> genericPropertyArray = new ArrayList<>();
        public List<ListOperation<SourceClassADto>> genericProperty = new ArrayList<>();

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder propertyArray(List<ListOperation<SourceClassADto>> propertyArray){
            this.propertyArray = propertyArray == null ? new ArrayList<>() : propertyArray;
            return this;
        }

        public Builder property(Option<SourceClassADto> property){
            this.property = property == null ? Option.some(null) : property;
            return this;
        }

        public Builder genericPropertyArray(List<ListOperation<List<ListOperation<SourceClassADto>>>> genericPropertyArray){
            this.genericPropertyArray = genericPropertyArray == null ? new ArrayList<>() : genericPropertyArray;
            return this;
        }

        public Builder genericProperty(List<ListOperation<SourceClassADto>> genericProperty){
            this.genericProperty = genericProperty == null ? new ArrayList<>() : genericProperty;
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

    public static class Conf extends DtoConf<SourceClassBDto>{

        public DtoListFieldConf<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassADto.Conf> propertyArray;
        public DtoFieldConf<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassADto.Conf> property;
        public ListFieldConf genericPropertyArray;
        public DtoListFieldConf<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassADto.Conf> genericProperty;

        public Conf(){}

        public static class Builder{

            public DtoListFieldConf<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassADto.Conf> propertyArray;
            public DtoFieldConf<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassADto.Conf> property;
            public ListFieldConf genericPropertyArray;
            public DtoListFieldConf<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassADto.Conf> genericProperty;

            public static  Builder newInstance(){
                return new Builder();
            }

            public Builder propertyArray(DtoListFieldConf<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassADto.Conf> propertyArray){
                this.propertyArray = propertyArray;
                return this;
            }

            public Builder property(DtoFieldConf<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassADto.Conf> property){
                this.property = property;
                return this;
            }

            public Builder genericPropertyArray(ListFieldConf genericPropertyArray){
                this.genericPropertyArray = genericPropertyArray;
                return this;
            }

            public Builder genericProperty(DtoListFieldConf<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassADto.Conf> genericProperty){
                this.genericProperty = genericProperty;
                return this;
            }

            public SourceClassBDto.Conf build(){
                SourceClassBDto.Conf instance = new SourceClassBDto.Conf();
                instance.propertyArray = propertyArray;
                instance.property = property;
                instance.genericPropertyArray = genericPropertyArray;
                instance.genericProperty = genericProperty;
                return instance;
            }

        }


    }

}