package solutions.sulfura.gend.dtos.circular_dependencies.class_b;

import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.circular_dependencies.class_b.SourceClassB;
import solutions.sulfura.gend.dtos.projection.DtoProjectionException;
import solutions.sulfura.gend.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.gend.dtos.projection.DtoProjection;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.circular_dependencies.class_a.SourceClassADto.Projection;
import solutions.sulfura.gend.dtos.circular_dependencies.class_a.SourceClassADto;
import solutions.sulfura.gend.dtos.projection.ProjectionFor;
import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.ListOperation;
import java.util.List;
import solutions.sulfura.gend.dtos.projection.fields.ListFieldConf;
import solutions.sulfura.gend.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.gend.dtos.projection.ProjectionUtils;
import solutions.sulfura.gend.dtos.projection.fields.FieldConf.Presence;

@DtoFor(SourceClassB.class)
public class SourceClassBDto implements Dto<SourceClassB>{

    public Option<SourceClassADto> property = Option.none();
    public Option<List<ListOperation<SourceClassADto>>> propertyArray = Option.none();
    public Option<List<ListOperation<SourceClassADto>>> genericProperty = Option.none();
    public Option<List<ListOperation<List<ListOperation<SourceClassADto>>>>> genericPropertyArray = Option.none();

    public SourceClassBDto() {
    }

    public Class<SourceClassB> getSourceClass() {
        return SourceClassB.class;
    }

    public static class Builder{

        Option<SourceClassADto> property = Option.none();
        Option<List<ListOperation<SourceClassADto>>> propertyArray = Option.none();
        Option<List<ListOperation<SourceClassADto>>> genericProperty = Option.none();
        Option<List<ListOperation<List<ListOperation<SourceClassADto>>>>> genericPropertyArray = Option.none();

        public static  Builder newInstance(){
            return new Builder();
        }

        public Builder property(final Option<SourceClassADto> property){
            this.property = property == null ? Option.none() : property;
            return this;
        }

        public Builder propertyArray(final Option<List<ListOperation<SourceClassADto>>> propertyArray){
            this.propertyArray = propertyArray == null ? Option.none() : propertyArray;
            return this;
        }

        public Builder genericProperty(final Option<List<ListOperation<SourceClassADto>>> genericProperty){
            this.genericProperty = genericProperty == null ? Option.none() : genericProperty;
            return this;
        }

        public Builder genericPropertyArray(final Option<List<ListOperation<List<ListOperation<SourceClassADto>>>>> genericPropertyArray){
            this.genericPropertyArray = genericPropertyArray == null ? Option.none() : genericPropertyArray;
            return this;
        }


        public SourceClassBDto build(){

            SourceClassBDto instance = new SourceClassBDto();
            instance.property = property;
            instance.propertyArray = propertyArray;
            instance.genericProperty = genericProperty;
            instance.genericPropertyArray = genericPropertyArray;

            return instance;

        }

    }

    @ProjectionFor(SourceClassBDto.class)
    public static class Projection extends DtoProjection<SourceClassBDto> {

        public DtoFieldConf<SourceClassADto.Projection> property;
        public DtoListFieldConf<SourceClassADto.Projection> propertyArray;
        public DtoListFieldConf<SourceClassADto.Projection> genericProperty;
        public ListFieldConf genericPropertyArray;

        public Projection() {
        }

        public void applyProjectionTo(SourceClassBDto dto) throws DtoProjectionException {
            dto.property = ProjectionUtils.getProjectedValue(dto.property, this.property);
            dto.propertyArray = ProjectionUtils.getProjectedValue(dto.propertyArray, this.propertyArray);
            dto.genericProperty = ProjectionUtils.getProjectedValue(dto.genericProperty, this.genericProperty);
            dto.genericPropertyArray = ProjectionUtils.getProjectedValue(dto.genericPropertyArray, this.genericPropertyArray);
        }

        public static class Builder{

            DtoFieldConf<SourceClassADto.Projection> property;
            DtoListFieldConf<SourceClassADto.Projection> propertyArray;
            DtoListFieldConf<SourceClassADto.Projection> genericProperty;
            ListFieldConf genericPropertyArray;

            public static  Builder newInstance(){
                return new Builder();
            }

            public Builder property(final DtoFieldConf<SourceClassADto.Projection> property){
                this.property = property;
                return this;
            }

            public Builder property(final Presence presence, final SourceClassADto.Projection projection){
                property = DtoFieldConf.of(presence, projection);
                return this;
            }

            public Builder propertyArray(final DtoListFieldConf<SourceClassADto.Projection> propertyArray){
                this.propertyArray = propertyArray;
                return this;
            }

            public Builder propertyArray(final Presence presence, final SourceClassADto.Projection projection){
                propertyArray = DtoListFieldConf.of(presence, projection);
                return this;
            }

            public Builder genericProperty(final DtoListFieldConf<SourceClassADto.Projection> genericProperty){
                this.genericProperty = genericProperty;
                return this;
            }

            public Builder genericProperty(final Presence presence, final SourceClassADto.Projection projection){
                genericProperty = DtoListFieldConf.of(presence, projection);
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

            public SourceClassBDto.Projection build(){

                SourceClassBDto.Projection instance = new SourceClassBDto.Projection();
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