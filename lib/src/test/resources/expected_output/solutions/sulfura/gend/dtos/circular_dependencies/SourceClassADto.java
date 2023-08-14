package solutions.sulfura.gend.dtos.circular_dependencies;

import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.conf.DtoConf;
import solutions.sulfura.gend.dtos.conf.fields.FieldConf;
import solutions.sulfura.gend.dtos.conf.fields.FieldConf.Presence;
import solutions.sulfura.gend.dtos.conf.fields.ListFieldConf;
import solutions.sulfura.gend.dtos.conf.fields.DtoFieldConf;
import solutions.sulfura.gend.dtos.conf.fields.DtoListFieldConf;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassBDto;
import java.util.List;
import solutions.sulfura.gend.dtos.ListOperation;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassA;

@DtoFor(SourceClassA.class)
public class SourceClassADto implements Dto<SourceClassA>{

    public Option<List<ListOperation<SourceClassBDto>>> propertyArray;
    public Option<SourceClassBDto> property;
    public Option<List<ListOperation<List<ListOperation<SourceClassBDto>>>>> genericPropertyArray;
    public Option<List<ListOperation<SourceClassBDto>>> genericProperty;

    public SourceClassADto(){}

    public static class Builder{

        public Option<List<ListOperation<SourceClassBDto>>> propertyArray;
        public Option<SourceClassBDto> property;
        public Option<List<ListOperation<List<ListOperation<SourceClassBDto>>>>> genericPropertyArray;
        public Option<List<ListOperation<SourceClassBDto>>> genericProperty;

        public static  Builder newInstance(){
            return new Builder();
        }

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

    public static class Conf<DtoConf> {

        public DtoListFieldConf<SourceClassBDto.Conf> propertyArray;
        public DtoFieldConf<SourceClassBDto.Conf> property;
        public ListFieldConf genericPropertyArray;
        public DtoListFieldConf<SourceClassBDto.Conf> genericProperty;

        public Conf(){}

        public static class Builder{

            public DtoListFieldConf<SourceClassBDto.Conf>  propertyArray;
            public DtoFieldConf<SourceClassBDto.Conf>;
            public ListFieldConf genericPropertyArray;
            public DtoListFieldConf<SourceClassBDto.Conf> genericProperty;

            public static  Builder newInstance(){
                return new Builder();
            }

            public Builder propertyArray(DtoListFieldConf<SourceClassBDto.Conf>  propertyArray){
                this.propertyArray = propertyArray;
                return this;
            }

            public Builder property(DtoFieldConf<SourceClassBDto.Conf> property){
                this.property = property;
                return this;
            }

            public Builder genericPropertyArray(ListFieldConf genericPropertyArray){
                this.genericPropertyArray = genericPropertyArray;
                return this;
            }

            public Builder genericProperty(DtoListFieldConf<SourceClassBDto.Conf> genericProperty){
                this.genericProperty = genericProperty;
                return this;
            }

            public Conf build(){

                Conf instance = new Conf();
                instance.propertyArray = propertyArray != null ? propertyArray : DtoListFieldConf.DtoListConfBuilder.newInstance().build();
                instance.property = property != null ? property : DtoFieldConf.DtoFieldConfBuilder.newInstance().build();
                instance.genericPropertyArray = genericPropertyArray != null ? genericPropertyArray : ListFieldConf.ListConfBuilder.newInstance().build();
                instance.genericProperty = genericProperty != null ? genericProperty : DtoListFieldConf.DtoListConfBuilder.newInstance().build();

                return instance;

            }

        }


    }

}