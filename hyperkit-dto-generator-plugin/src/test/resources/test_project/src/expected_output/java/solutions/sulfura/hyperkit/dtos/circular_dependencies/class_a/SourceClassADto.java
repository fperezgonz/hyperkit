package solutions.sulfura.hyperkit.dtos.circular_dependencies.class_a;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.hyperkit.dtos.circular_dependencies.class_a.SourceClassA;
import solutions.sulfura.hyperkit.dtos.circular_dependencies.class_b.SourceClassBDto;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import java.util.List;
import solutions.sulfura.hyperkit.dtos.projection.fields.ListFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;

@DtoFor(SourceClassA.class)
public class SourceClassADto implements Dto<SourceClassA> {

    public ValueWrapper<SourceClassBDto> property = ValueWrapper.empty();
    public ValueWrapper<List<ListOperation<SourceClassBDto>>> propertyArray = ValueWrapper.empty();
    public ValueWrapper<List<ListOperation<SourceClassBDto>>> genericProperty = ValueWrapper.empty();
    public ValueWrapper<List<ListOperation<List<ListOperation<SourceClassBDto>>>>> genericPropertyArray = ValueWrapper.empty();

    public SourceClassADto() {
    }

    public Class<SourceClassA> getSourceClass() {
        return SourceClassA.class;
    }

    public static class Builder {

        ValueWrapper<SourceClassBDto> property = ValueWrapper.empty();
        ValueWrapper<List<ListOperation<SourceClassBDto>>> propertyArray = ValueWrapper.empty();
        ValueWrapper<List<ListOperation<SourceClassBDto>>> genericProperty = ValueWrapper.empty();
        ValueWrapper<List<ListOperation<List<ListOperation<SourceClassBDto>>>>> genericPropertyArray = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder property(final ValueWrapper<SourceClassBDto> property){
            this.property = property == null ? ValueWrapper.empty() : property;
            return this;
        }

        public Builder propertyArray(final ValueWrapper<List<ListOperation<SourceClassBDto>>> propertyArray){
            this.propertyArray = propertyArray == null ? ValueWrapper.empty() : propertyArray;
            return this;
        }

        public Builder genericProperty(final ValueWrapper<List<ListOperation<SourceClassBDto>>> genericProperty){
            this.genericProperty = genericProperty == null ? ValueWrapper.empty() : genericProperty;
            return this;
        }

        public Builder genericPropertyArray(final ValueWrapper<List<ListOperation<List<ListOperation<SourceClassBDto>>>>> genericPropertyArray){
            this.genericPropertyArray = genericPropertyArray == null ? ValueWrapper.empty() : genericPropertyArray;
            return this;
        }


        public SourceClassADto build() {

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

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return property.equals(that.property)
                       && propertyArray.equals(that.propertyArray)
                       && genericProperty.equals(that.genericProperty)
                       && genericPropertyArray.equals(that.genericPropertyArray);

        }

        public static class Builder {

            DtoFieldConf<SourceClassBDto.Projection> property;
            DtoListFieldConf<SourceClassBDto.Projection> propertyArray;
            DtoListFieldConf<SourceClassBDto.Projection> genericProperty;
            ListFieldConf genericPropertyArray;

            public static Builder newInstance() {
                return new Builder();
            }

            public Builder property(final DtoFieldConf<SourceClassBDto.Projection> property){
                this.property = property;
                return this;
            }

            public Builder property(final Presence presence, final SourceClassBDto.Projection projection){
                property = DtoFieldConf.of(presence, projection);
                return this;
            }

            public Builder propertyArray(final DtoListFieldConf<SourceClassBDto.Projection> propertyArray){
                this.propertyArray = propertyArray;
                return this;
            }

            public Builder propertyArray(final Presence presence, final SourceClassBDto.Projection projection){
                propertyArray = DtoListFieldConf.of(presence, projection);
                return this;
            }

            public Builder genericProperty(final DtoListFieldConf<SourceClassBDto.Projection> genericProperty){
                this.genericProperty = genericProperty;
                return this;
            }

            public Builder genericProperty(final Presence presence, final SourceClassBDto.Projection projection){
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

            public SourceClassADto.Projection build() {

                SourceClassADto.Projection instance = new SourceClassADto.Projection();
                instance.property = property;
                instance.propertyArray = propertyArray;
                instance.genericProperty = genericProperty;
                instance.genericPropertyArray = genericPropertyArray;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _property = "property";
        public static final String _propertyArray = "propertyArray";
        public static final String _genericProperty = "genericProperty";
        public static final String _genericPropertyArray = "genericPropertyArray";

    }

}