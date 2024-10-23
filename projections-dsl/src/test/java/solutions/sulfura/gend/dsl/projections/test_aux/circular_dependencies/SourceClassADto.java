package solutions.sulfura.gend.dsl.projections.test_aux.circular_dependencies;

import solutions.sulfura.gend.dsl.projections.test_aux.dto_sources.circular_dependencies.SourceClassA;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.projection.ProjectionFor;
import solutions.sulfura.gend.dtos.projection.ProjectionUtils;
import solutions.sulfura.gend.dtos.projection.DtoProjectionException;
import solutions.sulfura.gend.dtos.projection.DtoProjection;
import solutions.sulfura.gend.dtos.projection.DtoProjectionException;
import solutions.sulfura.gend.dtos.projection.ProjectionUtils;
import solutions.sulfura.gend.dtos.projection.fields.FieldConf.Presence;
import solutions.sulfura.gend.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.gend.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.gend.dtos.projection.fields.ListFieldConf;
import java.util.List;
import solutions.sulfura.gend.dtos.ListOperation;
import io.vavr.control.Option;

@DtoFor(SourceClassA.class)
public class SourceClassADto implements Dto<SourceClassA>{

    public Option<List<ListOperation<SourceClassBDto>>> propertyArray = Option.none();
    public Option<SourceClassBDto> property = Option.none();
    public Option<List<ListOperation<List<ListOperation<SourceClassBDto>>>>> genericPropertyArray = Option.none();
    public Option<List<ListOperation<SourceClassBDto>>> genericProperty = Option.none();

    public SourceClassADto(){}

    public Class<SourceClassA> getSourceClass() {
        return SourceClassA.class;
    }

    public static class Builder{

        public Option<List<ListOperation<SourceClassBDto>>> propertyArray = Option.none();
        public Option<SourceClassBDto> property = Option.none();
        public Option<List<ListOperation<List<ListOperation<SourceClassBDto>>>>> genericPropertyArray = Option.none();
        public Option<List<ListOperation<SourceClassBDto>>> genericProperty = Option.none();

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder propertyArray(Option<List<ListOperation<SourceClassBDto>>> propertyArray){
            this.propertyArray = propertyArray == null ? Option.none() : propertyArray;
            return this;
        }

        public Builder property(Option<SourceClassBDto> property){
            this.property = property == null ? Option.none() : property;
            return this;
        }

        public Builder genericPropertyArray(Option<List<ListOperation<List<ListOperation<SourceClassBDto>>>>> genericPropertyArray){
            this.genericPropertyArray = genericPropertyArray == null ? Option.none() : genericPropertyArray;
            return this;
        }

        public Builder genericProperty(Option<List<ListOperation<SourceClassBDto>>> genericProperty){
            this.genericProperty = genericProperty == null ? Option.none() : genericProperty;
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

    @ProjectionFor(SourceClassADto.class)
    public static class Projection extends DtoProjection<SourceClassADto>{

        public DtoListFieldConf<solutions.sulfura.gend.dsl.projections.test_aux.circular_dependencies.SourceClassBDto.Projection> propertyArray;
        public DtoFieldConf<solutions.sulfura.gend.dsl.projections.test_aux.circular_dependencies.SourceClassBDto.Projection> property;
        public ListFieldConf genericPropertyArray;
        public DtoListFieldConf<solutions.sulfura.gend.dsl.projections.test_aux.circular_dependencies.SourceClassBDto.Projection> genericProperty;

        public void applyProjectionTo(SourceClassADto dto) throws DtoProjectionException {
            dto.propertyArray = ProjectionUtils.getProjectedValue(dto.propertyArray, this.propertyArray);
            dto.property = ProjectionUtils.getProjectedValue(dto.property, this.property);
            dto.genericPropertyArray = ProjectionUtils.getProjectedValue(dto.genericPropertyArray, this.genericPropertyArray);
            dto.genericProperty = ProjectionUtils.getProjectedValue(dto.genericProperty, this.genericProperty);
        }

        public Projection(){}

        public void applyProjectionTo(SourceClassADto dto) throws DtoProjectionException {
            dto.propertyArray = ProjectionUtils.getProjectedValue(dto.propertyArray, this.propertyArray);
            dto.property = ProjectionUtils.getProjectedValue(dto.property, this.property);
            dto.genericPropertyArray = ProjectionUtils.getProjectedValue(dto.genericPropertyArray, this.genericPropertyArray);
            dto.genericProperty = ProjectionUtils.getProjectedValue(dto.genericProperty, this.genericProperty);
        }

        public static class Builder{

            public DtoListFieldConf<solutions.sulfura.gend.dsl.projections.test_aux.circular_dependencies.SourceClassBDto.Projection> propertyArray;
            public DtoFieldConf<solutions.sulfura.gend.dsl.projections.test_aux.circular_dependencies.SourceClassBDto.Projection> property;
            public ListFieldConf genericPropertyArray;
            public DtoListFieldConf<solutions.sulfura.gend.dsl.projections.test_aux.circular_dependencies.SourceClassBDto.Projection> genericProperty;

            public static  Builder newInstance(){
                return new Builder();
            }

            public Builder propertyArray(DtoListFieldConf<solutions.sulfura.gend.dsl.projections.test_aux.circular_dependencies.SourceClassBDto.Projection> propertyArray){
                this.propertyArray = propertyArray;
                return this;
            }

            public Builder propertyArray(Presence presence, solutions.sulfura.gend.dsl.projections.test_aux.circular_dependencies.SourceClassBDto.Projection projection){
                propertyArray = DtoListFieldConf.of(presence, projection);
                return this;
            }

            public Builder property(DtoFieldConf<solutions.sulfura.gend.dsl.projections.test_aux.circular_dependencies.SourceClassBDto.Projection> property){
                this.property = property;
                return this;
            }

            public Builder property(Presence presence, solutions.sulfura.gend.dsl.projections.test_aux.circular_dependencies.SourceClassBDto.Projection projection){
                property = DtoFieldConf.of(presence, projection);
                return this;
            }

            public Builder genericPropertyArray(ListFieldConf genericPropertyArray){
                this.genericPropertyArray = genericPropertyArray;
                return this;
            }

            public Builder genericPropertyArray(Presence presence){
                genericPropertyArray = ListFieldConf.of(presence);
                return this;
            }

            public Builder genericProperty(DtoListFieldConf<solutions.sulfura.gend.dsl.projections.test_aux.circular_dependencies.SourceClassBDto.Projection> genericProperty){
                this.genericProperty = genericProperty;
                return this;
            }

            public Builder genericProperty(Presence presence, solutions.sulfura.gend.dsl.projections.test_aux.circular_dependencies.SourceClassBDto.Projection projection){
                genericProperty = DtoListFieldConf.of(presence, projection);
                return this;
            }

            public SourceClassADto.Projection build(){
                SourceClassADto.Projection instance = new SourceClassADto.Projection();
                instance.propertyArray = propertyArray;
                instance.property = property;
                instance.genericPropertyArray = genericPropertyArray;
                instance.genericProperty = genericProperty;
                return instance;
            }

        }


    }

    public static class DtoModel{

        public static final String _propertyArray = "propertyArray";
        public static final String _property = "property";
        public static final String _genericPropertyArray = "genericPropertyArray";
        public static final String _genericProperty = "genericProperty";

    }

}