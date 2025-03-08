package solutions.sulfura.gend.dtos.circular_dependencies;

import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassADto.Builder;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.projection.DtoProjectionException;
import solutions.sulfura.gend.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassA;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassBDto;
import solutions.sulfura.gend.dtos.projection.DtoProjection;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassADto.Projection;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassADto.DtoModel;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassBDto.Projection;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassADto;
import solutions.sulfura.gend.dtos.projection.ProjectionFor;
import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.ListOperation;
import java.util.List;
import solutions.sulfura.gend.dtos.projection.fields.ListFieldConf;
import solutions.sulfura.gend.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.gend.dtos.projection.ProjectionUtils;

@DtoFor(SourceClassA.class)
public class SourceClassADto implements Dto<SourceClassA>{

    public Option<SourceClassBDto> property = Option.none();
    public Option<List<ListOperation<SourceClassBDto>>> propertyArray = Option.none();
    public Option<List<ListOperation<SourceClassBDto>>> genericProperty = Option.none();
    public Option<List<ListOperation<List<ListOperation<SourceClassBDto>>>>> genericPropertyArray = Option.none();

    public SourceClassADto() {
    }

    public Class<SourceClassA> getSourceClass() {
        return SourceClassA.class;
    }

    public static class Builder{

        Option<SourceClassBDto> property = Option.none();
        Option<List<ListOperation<SourceClassBDto>>> propertyArray = Option.none();
        Option<List<ListOperation<SourceClassBDto>>> genericProperty = Option.none();
        Option<List<ListOperation<List<ListOperation<SourceClassBDto>>>>> genericPropertyArray = Option.none();

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder property(final Option<SourceClassBDto> property){
            this.property = property == null ? Option.none() : property;
            return this;
        }

        public Builder propertyArray(final Option<List<ListOperation<SourceClassBDto>>> propertyArray){
            this.propertyArray = propertyArray == null ? Option.none() : propertyArray;
            return this;
        }

        public Builder genericProperty(final Option<List<ListOperation<SourceClassBDto>>> genericProperty){
            this.genericProperty = genericProperty == null ? Option.none() : genericProperty;
            return this;
        }

        public Builder genericPropertyArray(final Option<List<ListOperation<List<ListOperation<SourceClassBDto>>>>> genericPropertyArray){
            this.genericPropertyArray = genericPropertyArray == null ? Option.none() : genericPropertyArray;
            return this;
        }


        public SourceClassADto build(){

            SourceClassADto instance = new SourceClassADto();
            instance.property = property;
            instance.propertyArray = propertyArray;
            instance.genericProperty = genericProperty;
            instance.genericPropertyArray = genericPropertyArray;

            return instance;

        }

    }

    @ProjectionFor(SourceClassADto.class)
    public static class Projection extends DtoProjection<SourceClassADto> {

        public DtoFieldConf<SourceClassBDto.Projection> property;
        public DtoListFieldConf<SourceClassBDto.Projection> propertyArray;
        public DtoListFieldConf<SourceClassBDto.Projection> genericProperty;
        public ListFieldConf genericPropertyArray;

        public Projection() {
        }

        public void applyProjectionTo(SourceClassADto dto) throws DtoProjectionException {
            dto.property = ProjectionUtils.getProjectedValue(dto.property, this.property);
            dto.propertyArray = ProjectionUtils.getProjectedValue(dto.propertyArray, this.propertyArray);
            dto.genericProperty = ProjectionUtils.getProjectedValue(dto.genericProperty, this.genericProperty);
            dto.genericPropertyArray = ProjectionUtils.getProjectedValue(dto.genericPropertyArray, this.genericPropertyArray);
        }

        public static class Builder{

            DtoFieldConf<SourceClassBDto.Projection> property;
            DtoListFieldConf<SourceClassBDto.Projection> propertyArray;
            DtoListFieldConf<SourceClassBDto.Projection> genericProperty;
            ListFieldConf genericPropertyArray;

            public static  Builder newInstance(){
                return new Builder();
            }

            public Builder property(final DtoFieldConf<SourceClassBDto.Projection> property){
                this.property = property;
                return this;
            }

            public Builder property(final Presence presence){
                property = DtoFieldConf<SourceClassBDto.Projection>.of(presence);
                return this;
            }

            public Builder propertyArray(final DtoListFieldConf<SourceClassBDto.Projection> propertyArray){
                this.propertyArray = propertyArray;
                return this;
            }

            public Builder propertyArray(final Presence presence){
                propertyArray = DtoListFieldConf<SourceClassBDto.Projection>.of(presence);
                return this;
            }

            public Builder genericProperty(final DtoListFieldConf<SourceClassBDto.Projection> genericProperty){
                this.genericProperty = genericProperty;
                return this;
            }

            public Builder genericProperty(final Presence presence){
                genericProperty = DtoListFieldConf<SourceClassBDto.Projection>.of(presence);
                return this;
            }

            public Builder genericPropertyArray(final ListFieldConf genericPropertyArray){
                this.genericPropertyArray = genericPropertyArray;
                return this;
            }

            public Builder genericPropertyArray(final Presence presence){
                genericPropertyArray = ListFieldConf.of(presence);
                return this;
            }

            public SourceClassADto.Projection build(){

                SourceClassADto.Projection instance = new SourceClassADto.Projection();
                instance.property = property;
                instance.propertyArray = propertyArray;
                instance.genericProperty = genericProperty;
                instance.genericPropertyArray = genericPropertyArray;

                return instance;

            }

        }

    }

    public static class DtoModel{

        public static final String _property = "property";
        public static final String _propertyArray = "propertyArray";
        public static final String _genericProperty = "genericProperty";
        public static final String _genericPropertyArray = "genericPropertyArray";

    }

}