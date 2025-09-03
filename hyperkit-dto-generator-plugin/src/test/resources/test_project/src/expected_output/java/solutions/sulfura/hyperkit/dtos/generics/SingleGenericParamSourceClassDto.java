package solutions.sulfura.hyperkit.dtos.generics;

import solutions.sulfura.hyperkit.dtos.Dto;
import java.util.Set;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.generics.SingleGenericParamSourceClass;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.projection.fields.ListFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import java.util.Objects;

@DtoFor(SingleGenericParamSourceClass.class)
public class SingleGenericParamSourceClassDto implements Dto<SingleGenericParamSourceClass> {

    public ValueWrapper<T> genericProperty = ValueWrapper.empty();
    public ValueWrapper<Set<ListOperation<T>>> nestedGenericProperty = ValueWrapper.empty();
    public ValueWrapper<T> genericPropertyWithGetter = ValueWrapper.empty();
    public ValueWrapper<T> genericPropertyWithSetter = ValueWrapper.empty();

    public SingleGenericParamSourceClassDto() {
    }

    public Class<SingleGenericParamSourceClass> getSourceClass() {
        return SingleGenericParamSourceClass.class;
    }

    public static class Builder {

        ValueWrapper<T> genericProperty = ValueWrapper.empty();
        ValueWrapper<Set<ListOperation<T>>> nestedGenericProperty = ValueWrapper.empty();
        ValueWrapper<T> genericPropertyWithGetter = ValueWrapper.empty();
        ValueWrapper<T> genericPropertyWithSetter = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder genericProperty(final ValueWrapper<T> genericProperty){
            this.genericProperty = genericProperty == null ? ValueWrapper.empty() : genericProperty;
            return this;
        }

        public Builder nestedGenericProperty(final ValueWrapper<Set<ListOperation<T>>> nestedGenericProperty){
            this.nestedGenericProperty = nestedGenericProperty == null ? ValueWrapper.empty() : nestedGenericProperty;
            return this;
        }

        public Builder genericPropertyWithGetter(final ValueWrapper<T> genericPropertyWithGetter){
            this.genericPropertyWithGetter = genericPropertyWithGetter == null ? ValueWrapper.empty() : genericPropertyWithGetter;
            return this;
        }

        public Builder genericPropertyWithSetter(final ValueWrapper<T> genericPropertyWithSetter){
            this.genericPropertyWithSetter = genericPropertyWithSetter == null ? ValueWrapper.empty() : genericPropertyWithSetter;
            return this;
        }


        public SingleGenericParamSourceClassDto build() {

            SingleGenericParamSourceClassDto instance = new SingleGenericParamSourceClassDto();
            instance.genericProperty = genericProperty;
            instance.nestedGenericProperty = nestedGenericProperty;
            instance.genericPropertyWithGetter = genericPropertyWithGetter;
            instance.genericPropertyWithSetter = genericPropertyWithSetter;

            return instance;

        }

    }

    @ProjectionFor(SingleGenericParamSourceClassDto.class)
    public static class Projection extends DtoProjection<SingleGenericParamSourceClassDto> {

        public FieldConf genericProperty;
        public ListFieldConf nestedGenericProperty;
        public FieldConf genericPropertyWithGetter;
        public FieldConf genericPropertyWithSetter;

        public Projection() {
        }

        public void applyProjectionTo(SingleGenericParamSourceClassDto dto) throws DtoProjectionException {
            dto.genericProperty = ProjectionUtils.getProjectedValue(dto.genericProperty, this.genericProperty);
            dto.nestedGenericProperty = ProjectionUtils.getProjectedValue(dto.nestedGenericProperty, this.nestedGenericProperty);
            dto.genericPropertyWithGetter = ProjectionUtils.getProjectedValue(dto.genericPropertyWithGetter, this.genericPropertyWithGetter);
            dto.genericPropertyWithSetter = ProjectionUtils.getProjectedValue(dto.genericPropertyWithSetter, this.genericPropertyWithSetter);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return genericProperty.equals(that.genericProperty)
                       && nestedGenericProperty.equals(that.nestedGenericProperty)
                       && genericPropertyWithGetter.equals(that.genericPropertyWithGetter)
                       && genericPropertyWithSetter.equals(that.genericPropertyWithSetter);

        }

        @Override
        public int hashCode() {
            return Objects.hash(genericProperty,
                    nestedGenericProperty,
                    genericPropertyWithGetter,
                    genericPropertyWithSetter);
        }

        public static class Builder {

            FieldConf genericProperty;
            ListFieldConf nestedGenericProperty;
            FieldConf genericPropertyWithGetter;
            FieldConf genericPropertyWithSetter;

            public static Builder newInstance() {
                return new Builder();
            }

            public Builder genericProperty(final FieldConf genericProperty){
                this.genericProperty = genericProperty;
                return this;
            }

            public Builder genericProperty(final Presence presence){
                genericProperty = FieldConf.of(presence);
                return this;
            }

            public Builder nestedGenericProperty(final ListFieldConf nestedGenericProperty){
                this.nestedGenericProperty = nestedGenericProperty;
                return this;
            }

            public Builder nestedGenericProperty(final Presence presence){
                nestedGenericProperty = ListFieldConf.of(presence);
                return this;
            }

            public Builder genericPropertyWithGetter(final FieldConf genericPropertyWithGetter){
                this.genericPropertyWithGetter = genericPropertyWithGetter;
                return this;
            }

            public Builder genericPropertyWithGetter(final Presence presence){
                genericPropertyWithGetter = FieldConf.of(presence);
                return this;
            }

            public Builder genericPropertyWithSetter(final FieldConf genericPropertyWithSetter){
                this.genericPropertyWithSetter = genericPropertyWithSetter;
                return this;
            }

            public Builder genericPropertyWithSetter(final Presence presence){
                genericPropertyWithSetter = FieldConf.of(presence);
                return this;
            }

            public SingleGenericParamSourceClassDto.Projection build() {

                SingleGenericParamSourceClassDto.Projection instance = new SingleGenericParamSourceClassDto.Projection();
                instance.genericProperty = genericProperty;
                instance.nestedGenericProperty = nestedGenericProperty;
                instance.genericPropertyWithGetter = genericPropertyWithGetter;
                instance.genericPropertyWithSetter = genericPropertyWithSetter;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _genericProperty = "genericProperty";
        public static final String _nestedGenericProperty = "nestedGenericProperty";
        public static final String _genericPropertyWithGetter = "genericPropertyWithGetter";
        public static final String _genericPropertyWithSetter = "genericPropertyWithSetter";

    }

}