package solutions.sulfura.hyperkit.dtos;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.aux_classes.NestingClass;
import solutions.sulfura.hyperkit.dtos.aux_classes.NestingClass.NestedClass;
import solutions.sulfura.hyperkit.dtos.SourceClassWithReferenceToNestedClass;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;

@DtoFor(SourceClassWithReferenceToNestedClass.class)
public class SourceClassWithReferenceToNestedClassDto implements Dto<SourceClassWithReferenceToNestedClass> {

    public ValueWrapper<NestedClass> nestedClassReference = ValueWrapper.empty();

    public SourceClassWithReferenceToNestedClassDto() {
    }

    public Class<SourceClassWithReferenceToNestedClass> getSourceClass() {
        return SourceClassWithReferenceToNestedClass.class;
    }

    public static class Builder {

        ValueWrapper<NestedClass> nestedClassReference = ValueWrapper.empty();

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder nestedClassReference(final ValueWrapper<NestedClass> nestedClassReference){
            this.nestedClassReference = nestedClassReference == null ? ValueWrapper.empty() : nestedClassReference;
            return this;
        }


        public SourceClassWithReferenceToNestedClassDto build() {

            SourceClassWithReferenceToNestedClassDto instance = new SourceClassWithReferenceToNestedClassDto();
            instance.nestedClassReference = nestedClassReference;

            return instance;

        }

    }

    @ProjectionFor(SourceClassWithReferenceToNestedClassDto.class)
    public static class Projection extends DtoProjection<SourceClassWithReferenceToNestedClassDto> {

        public FieldConf nestedClassReference;

        public Projection() {
        }

        public void applyProjectionTo(SourceClassWithReferenceToNestedClassDto dto) throws DtoProjectionException {
            dto.nestedClassReference = ProjectionUtils.getProjectedValue(dto.nestedClassReference, this.nestedClassReference);
        }

        @Override
        public boolean equals(Object o) {

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Projection that = (Projection) o;

            return nestedClassReference.equals(that.nestedClassReference);

        }

        public static class Builder {

            FieldConf nestedClassReference;

            public static Builder newInstance() {
                return new Builder();
            }

            public Builder nestedClassReference(final FieldConf nestedClassReference){
                this.nestedClassReference = nestedClassReference;
                return this;
            }

            public Builder nestedClassReference(final Presence presence){
                nestedClassReference = FieldConf.of(presence);
                return this;
            }

            public SourceClassWithReferenceToNestedClassDto.Projection build() {

                SourceClassWithReferenceToNestedClassDto.Projection instance = new SourceClassWithReferenceToNestedClassDto.Projection();
                instance.nestedClassReference = nestedClassReference;

                return instance;

            }

        }

    }

    public static class DtoModel {

        public static final String _nestedClassReference = "nestedClassReference";

    }

}